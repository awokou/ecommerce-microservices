package com.server.userservice.controller;

import com.server.userservice.domain.dto.external.*;
import com.server.userservice.domain.dto.response.AuthResponse;
import com.server.userservice.domain.dto.response.UserResponse;
import com.server.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUser(@Valid @RequestBody UserDto request) {
        log.info("Registration request for username: {}", request.getEmail());
        AuthResponse response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
}