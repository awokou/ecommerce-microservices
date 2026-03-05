package com.server.userservice.service.impl;

import com.server.userservice.domain.dto.request.ResendEmailConfirmationRequest;
import com.server.userservice.domain.dto.request.ResetPasswordRequest;
import com.server.userservice.domain.dto.response.AuthResponse;
import com.server.userservice.domain.dto.request.LoginRequest;
import com.server.userservice.domain.dto.request.RegisterRequest;
import com.server.userservice.domain.dto.response.UserResponse;
import com.server.userservice.domain.dto.response.ValidateTokenResponse;
import com.server.userservice.domain.entity.ConfirmationEmail;
import com.server.userservice.domain.enums.ResultCode;
import com.server.userservice.domain.enums.Role;
import com.server.userservice.domain.entity.User;
import com.server.userservice.exception.AlreadyExistException;
import com.server.userservice.exception.BadRequestException;
import com.server.userservice.exception.ResourceNotFoundException;
import com.server.userservice.repository.ConfirmationEmailRepository;
import com.server.userservice.repository.UserRepository;
import com.server.userservice.security.JwtUtils;
import com.server.userservice.service.MailService;
import com.server.userservice.service.UserService;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationEmailRepository confirmationEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getEmail());
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistException("Email already exists: " + request.getEmail());
        }
        // Create new user
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();

        userRepository.save(user);
        mailService.sendConfirmRegistrationAccount(user.getEmail(), user.getFirstName() + " " + user.getLastName());
        var jwtToken = jwtUtils.generateToken(user);
        log.info("User registered successfully: {}", user.getUsername());

        return AuthResponse.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .token(jwtToken)
                .expiresIn(jwtUtils.getExpirationTime())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
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
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @Override
    public UserResponse findAllById(Long userId) {
        return this.userRepository.findById(userId)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found" + userId));
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
    public void resetPassword(ResetPasswordRequest reset) {

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
    public void resendEmailConfirmation(ResendEmailConfirmationRequest request) {
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

    private UserResponse mapToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}