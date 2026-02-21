package com.server.userservice.service;

import com.server.userservice.dto.AuthResponse;
import com.server.userservice.dto.LoginRequest;
import com.server.userservice.dto.RegisterRequest;
import com.server.userservice.dto.ValidateTokenResponse;
import com.server.userservice.entity.Role;
import com.server.userservice.entity.User;
import com.server.userservice.repository.UserRepository;
import com.server.userservice.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getEmail());

        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        var jwtToken = jwtUtils.generateToken(user);

        log.info("User registered successfully: {}", user.getUsername());

        return AuthResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(jwtToken)
                .expiresIn(jwtUtils.getExpirationTime())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        log.info("User login attempt: {}", request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var jwtToken = jwtUtils.generateToken(user);

        log.info("User logged in successfully: {}", user.getUsername());

        return AuthResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(jwtToken)
                .expiresIn(jwtUtils.getExpirationTime())
                .build();
    }

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
}