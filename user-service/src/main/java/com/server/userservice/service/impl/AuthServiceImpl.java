package com.server.userservice.service.impl;

import com.server.userservice.domain.dto.external.LoginDto;
import com.server.userservice.domain.dto.external.ResendEmailConfirmationDto;
import com.server.userservice.domain.dto.external.ResetPasswordDto;
import com.server.userservice.domain.dto.response.AuthResponse;
import com.server.userservice.domain.dto.response.ValidateTokenResponse;
import com.server.userservice.domain.entity.ConfirmationEmail;
import com.server.userservice.domain.entity.User;
import com.server.userservice.domain.enums.ResultCode;
import com.server.userservice.exception.BadRequestException;
import com.server.userservice.repository.ConfirmationEmailRepository;
import com.server.userservice.repository.UserRepository;
import com.server.userservice.security.JwtUtils;
import com.server.userservice.service.AuthService;
import com.server.userservice.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl  implements AuthService {

    private final UserRepository userRepository;
    private final ConfirmationEmailRepository confirmationEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    @Override
    public AuthResponse login(LoginDto request) {
        try {
            log.info("User login attempt: {}", request.getEmail());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!user.isEnabled()) {
                throw new BadRequestException(ResultCode.EMAIL_NOT_VERIFIED, "Email not verified");
            }

            var jwtToken = jwtUtils.generateToken(user);
            log.info("User logged in successfully: {}", user.getUsername());
            updateUserLastLoginDate(user);

            return AuthResponse.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .role(user.getRole().name())
                    .token(jwtToken)
                    .expiresIn(jwtUtils.getExpirationTime())
                    .build();

        } catch (UsernameNotFoundException e) {
            log.warn("Login attempt with non-existent email: {}", request.getEmail());
            throw new BadRequestException(ResultCode.AUTHENTICATION_FAILED);
        }
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        ConfirmationEmail confirmationEmail = validateRegistrationToken(token);
        User user = userRepository.findByEmail(confirmationEmail.getEmail())
                .orElseThrow(() -> new BadRequestException(ResultCode.USER_NOT_FOUND_OR_DISABLED));

        user.setEnabled(true);
        confirmationEmail.setConfirmedAt(LocalDateTime.now());

        userRepository.save(user);
        confirmationEmailRepository.save(confirmationEmail);
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException(ResultCode.BAD_REQUEST));

        var jwtToken = jwtUtils.generateToken(user);

        ConfirmationEmail savedConfirmationEmail = ConfirmationEmail.builder()
                .email(email)
                .token(jwtToken)
                .revoked(true)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();

        confirmationEmailRepository.save(savedConfirmationEmail);
    }

    // This method allows users to reset their password using a valid token sent to
    // their email
    @Override
    @Transactional
    public void resetPassword(ResetPasswordDto reset) {

        ConfirmationEmail confirmationEmail = validateResetPasswordToken(reset.getToken());
        confirmationEmail.setConfirmedAt(LocalDateTime.now());

        User user = userRepository.findByEmail(confirmationEmail.getEmail())
                .orElseThrow(() -> new BadRequestException(ResultCode.USER_NOT_FOUND_OR_DISABLED));

        user.setPassword(passwordEncoder.encode(reset.getPassword()));
        confirmationEmailRepository.save(confirmationEmail);
        userRepository.save(user);
    }

    // This method allows users to request a new email confirmation if they haven't
    // verified their email yet
    @Override
    @Transactional
    public void resendEmailConfirmation(ResendEmailConfirmationDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException(ResultCode.USER_NOT_FOUND_OR_DISABLED));

        if (user.isEnabled()) {
            throw new BadRequestException(ResultCode.EMAIL_ALREADY_VERIFIED);
        }

        ConfirmationEmail resendEmailConfirmation = ConfirmationEmail.builder()
                .revoked(true)
                .build();

        confirmationEmailRepository.save(resendEmailConfirmation);

        try {
            mailService.sendConfirmRegistrationAccount(user.getEmail(), user.getFirstName());
        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }

    // This method is used to validate the JWT token and return the associated user
    // information
    @Override
    public ValidateTokenResponse validateToken(String token) {
        try {
            String email = jwtUtils.extractUsername(token);
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            boolean isValid = jwtUtils.isTokenValid(token, user);

            return ValidateTokenResponse.builder()
                    .valid(isValid)
                    .email(email)
                    .role(user.getRole().name())
                    .build();

        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return ValidateTokenResponse.builder()
                    .valid(false)
                    .build();
        }
    }

    private void updateUserLastLoginDate(User user) {
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    // Helper method to validate registration and reset password tokens
    public ConfirmationEmail validateRegistrationToken(String token) {
        ConfirmationEmail confirmationEmail = validateBaseToken(token);
        if (confirmationEmail.getConfirmedAt() != null) {
            throw new BadRequestException(ResultCode.EMAIL_ALREADY_VERIFIED);
        }

        return confirmationEmail;
    }

    // Helper method to validate reset password token
    public ConfirmationEmail validateResetPasswordToken(String token) {
        ConfirmationEmail confirmationEmail = validateBaseToken(token);
        if (confirmationEmail.getConfirmedAt() != null) {
            throw new BadRequestException(ResultCode.TOKEN_RESET_PASSWORD_USED);
        }

        return confirmationEmail;
    }

    // Common validation logic for both registration and reset password tokens
    private ConfirmationEmail validateBaseToken(String token) {
        ConfirmationEmail confirmationEmail = confirmationEmailRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException(ResultCode.INVALID_TOKEN));

        if (confirmationEmail.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException(ResultCode.TOKEN_EXPIRED);
        }

        if (confirmationEmail.isRevoked()) {
            throw new BadRequestException(ResultCode.INVALID_TOKEN);
        }

        return confirmationEmail;
    }
}
