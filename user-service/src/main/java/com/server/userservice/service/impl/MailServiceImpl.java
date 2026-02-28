package com.server.userservice.service.impl;

import com.server.userservice.domain.entity.ConfirmationEmail;
import com.server.userservice.domain.enums.NotificationType;
import com.server.userservice.domain.event.NotificationEvent;
import com.server.userservice.kafka.UserProducer;
import com.server.userservice.repository.ConfirmationEmailRepository;
import com.server.userservice.service.MailService;
import com.server.userservice.utils.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${client.base.url}")
    private String CLIENT_BASE_URL;

    public static final String EMAIL_CHANNEL_NAME = "EMAIL";
    public static final String RECIPIENT_NAME = "recipientName";
    public static final String TOKEN_URL = "tokenUrl";
    public static final String OTP = "otp";

    private final UserProducer userProducer;
    private final ConfirmationEmailRepository confirmationEmailRepository;

    @Override
    public void sendEmailConfirmation(String email, String recipientName) {
        String otp = CommonUtil.getRandomNum();  // 6 digits
        String token = CommonUtil.sha256Hash(otp + secretKey);

        saveConfirmationEmail(token, email);

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel(EMAIL_CHANNEL_NAME)
                .recipient(email)
                .notificationType(NotificationType.CONFIRM_REGISTRATION_ACCOUNT)
                .params(Map.of(
                        RECIPIENT_NAME, recipientName,
                        TOKEN_URL, buildEmailUrl(token),
                        OTP, otp
                ))
                .build();

        userProducer.sendConfirmRegistrationAccount(notificationEvent);
    }

    private void saveConfirmationEmail(String token, String email) {
        // revoke all previous confirmation email
        ConfirmationEmail confirmationEmail = ConfirmationEmail.builder()
                .token(token)
                .email(email)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .revoked(false)
                .build();

        confirmationEmailRepository.save(confirmationEmail);
    }

    private String buildEmailUrl(String token) {
        return CLIENT_BASE_URL + "/auth/verify-email/request?token=" + token;
    }
}
