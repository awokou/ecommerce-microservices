package com.server.userservice.controller;

import com.server.userservice.dto.*;
import com.server.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request for username: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for username: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validateToken(@RequestBody ValidateTokenRequest request) {
        log.info("Token validation request");
        ValidateTokenResponse response = authService.validateToken(request.getToken());
        return ResponseEntity.ok(response);
    }
}