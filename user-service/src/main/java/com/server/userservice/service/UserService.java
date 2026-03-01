package com.server.userservice.service;

import com.server.userservice.domain.dto.request.LoginRequest;
import com.server.userservice.domain.dto.request.RegisterRequest;
import com.server.userservice.domain.dto.request.ResendEmailConfirmationRequest;
import com.server.userservice.domain.dto.request.ResetPasswordRequest;
import com.server.userservice.domain.dto.response.AuthResponse;
import com.server.userservice.domain.dto.response.UserResponse;
import com.server.userservice.domain.dto.response.ValidateTokenResponse;

import java.util.List;

public interface UserService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    List<UserResponse> findAll();

    void verifyEmail(String token);

    void forgotPassword(String email);

    void resetPassword(ResetPasswordRequest reset);

    void resendEmailConfirmation(ResendEmailConfirmationRequest request);

    ValidateTokenResponse validateToken(String token);
}
