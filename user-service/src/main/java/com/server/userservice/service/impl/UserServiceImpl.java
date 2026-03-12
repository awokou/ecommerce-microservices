package com.server.userservice.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.userservice.domain.dto.external.UserDto;
import com.server.userservice.domain.dto.response.AuthResponse;
import com.server.userservice.domain.dto.response.UserResponse;
import com.server.userservice.domain.entity.User;
import com.server.userservice.domain.enums.Role;
import com.server.userservice.exception.AlreadyExistException;
import com.server.userservice.exception.ResourceNotFoundException;
import com.server.userservice.repository.UserRepository;
import com.server.userservice.security.JwtUtils;
import com.server.userservice.service.MailService;
import com.server.userservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final MailService mailService;

    @Override
    @Transactional
    public AuthResponse createUser(UserDto request) {
        log.info("Registering new user: {}", request.getEmail());
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistException("Email already exists: " + request.getEmail());
        }
        // Create new user
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
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
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender().name())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .token(jwtToken)
                .expiresIn(jwtUtils.getExpirationTime())
                .build();
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

    private UserResponse mapToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender().name())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}