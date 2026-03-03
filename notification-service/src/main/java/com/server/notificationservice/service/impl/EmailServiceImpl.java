package com.server.notificationservice.service.impl;

import com.server.notificationservice.domain.dto.Product;
import com.server.notificationservice.domain.enums.EmailTemplates;
import com.server.notificationservice.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    @Override
    public void sendPaymentSuccessEmail(String email, String name, BigDecimal amount, String orderNumber) {

        try {
            Map<String, Object> variablesMap = new HashMap<>();
            variablesMap.put("name", name);
            variablesMap.put("amount", amount);
            variablesMap.put("orderNumber", orderNumber);

            Context context = new Context();
            context.setVariables(variablesMap);

            final String templateName = EmailTemplates.PAYMENT_CONFIRMATION.getTemplate();
            String htmlTemplate = templateEngine.process(templateName, context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setSubject(EmailTemplates.PAYMENT_CONFIRMATION.getSubject());
            helper.setFrom(senderEmail);
            helper.setTo(email);
            helper.setText(htmlTemplate, true);

            javaMailSender.send(mimeMessage);
            log.info("Successfully sent email to {} with template {}", email, templateName);
        } catch (MessagingException e) {
            log.warn("Failed to send payment confirmation email to {}", email);
        }
    }

    @Async
    @Override
    public void sendOrderConfirmationEmail(String email, String name, BigDecimal totalAmount, String orderNumber, List<Product> products) {

        try {
            Map<String, Object> variablesMap = new HashMap<>();
            variablesMap.put("name", name);
            variablesMap.put("totalAmount", totalAmount);
            variablesMap.put("orderNumber", orderNumber);
            variablesMap.put("products", products);

            Context context = new Context();
            context.setVariables(variablesMap);

            final String templateName = EmailTemplates.ORDER_CONFIRMATION.getTemplate();
            String htmlTemplate = templateEngine.process(templateName, context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setSubject(EmailTemplates.ORDER_CONFIRMATION.getSubject());
            helper.setFrom(senderEmail);
            helper.setTo(email);
            helper.setText(htmlTemplate, true);

            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully to {} with template {}", email, templateName);
        } catch (MessagingException e) {
            log.warn("Failed to send order confirmation email to {}", email);
        }
    }
}
