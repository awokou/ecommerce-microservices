package com.server.userservice.controller;

import com.server.userservice.domain.dto.external.*;
import com.server.userservice.domain.dto.response.AuthResponse;
import com.server.userservice.domain.dto.response.ValidateTokenResponse;
import com.server.userservice.service.AuthService;
import com.server.userservice.utils.CommonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${application.security.jwt.secret}")
    private String secretKey;

    private final AuthService authService;

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginDto request) {
        log.info("Login request for username: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // Endpoint for email verification with OTP
    @PostMapping("/verify-email/with-otp")
    public ResponseEntity<Void> verifyEmailWithOtp(@RequestBody @Valid VerifyEmailWithOtpDto request) {
        String token = CommonUtil.sha256Hash(request.getOtp() + secretKey);
        authService.verifyEmail(token);
        return ResponseEntity.noContent().build();
    }

    // Endpoint for email verification with token
    @PostMapping("/verify-email/with-token")
    public ResponseEntity<Void> verifyEmailWithToken(@RequestBody @Valid ValidateTokenDto request) {
        authService.verifyEmail(request.getToken());
        return ResponseEntity.noContent().build();
    }

    // Endpoint for email confirmation
    @PostMapping("/resend-email-confirmation")
    public ResponseEntity<Void> resendEmailConfirmation(@RequestBody @Valid ResendEmailConfirmationDto request) {
        authService.resendEmailConfirmation(request);
        return ResponseEntity.noContent().build();
    }

    // Endpoint for forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordDto request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.noContent().build();
    }

    // Endpoint for resetting password
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordDto request) {
        authService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }

    // Endpoint for validating JWT token
    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validateToken(@RequestBody ValidateTokenDto request) {
        log.info("Token validation request");
        ValidateTokenResponse response = authService.validateToken(request.getToken());
        return ResponseEntity.ok(response);
    }
}
