package com.server.userservice.controller;

import com.server.userservice.domain.dto.request.*;
import com.server.userservice.domain.dto.response.AuthResponse;
import com.server.userservice.domain.dto.response.UserResponse;
import com.server.userservice.domain.dto.response.ValidateTokenResponse;
import com.server.userservice.service.UserService;
import com.server.userservice.utils.CommonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Value("${jwt.secret}")
    private String secretKey;

    private final UserService userService;

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request for username: {}", request.getEmail());
        AuthResponse response = userService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for username: {}", request.getEmail());
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    // Endpoint to get all users
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> response = userService.findAll();
        return ResponseEntity.ok(response);
    }

    // Endpoint to get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> findAllById(@PathVariable Long userId) {
        UserResponse response = userService.findAllById(userId);
        return ResponseEntity.ok(response);
    }

    // Endpoint for email verification with OTP
    @PostMapping("/verify-email/with-otp")
    public ResponseEntity<Void> verifyEmailWithOtp(@RequestBody @Valid VerifyEmailWithOtpRequest request) {
        String token = CommonUtil.sha256Hash(request.otp() + secretKey);
        userService.verifyEmail(token);
        return ResponseEntity.noContent().build();
    }


    // Endpoint for email verification with token
    @PostMapping("/verify-email/with-token")
    public ResponseEntity<Void> verifyEmailWithToken(@RequestBody @Valid ValidateTokenRequest request) {
        userService.verifyEmail(request.getToken());
        return ResponseEntity.noContent().build();
    }

    // Endpoint for email confirmation
    @PostMapping("/resend-email-confirmation")
    public ResponseEntity<Void> resendEmailConfirmation(@RequestBody @Valid ResendEmailConfirmationRequest request) {
        userService.resendEmailConfirmation(request);
        return ResponseEntity.noContent().build();
    }

    // Endpoint for forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        userService.forgotPassword(request.getEmail());
        return ResponseEntity.noContent().build();
    }

    // Endpoint for resetting password
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }

    // Endpoint for validating JWT token
    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validateToken(@RequestBody ValidateTokenRequest request) {
        log.info("Token validation request");
        ValidateTokenResponse response = userService.validateToken(request.getToken());
        return ResponseEntity.ok(response);
    }
}