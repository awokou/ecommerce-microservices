package com.server.userservice.service;

import com.server.userservice.domain.dto.request.LoginRequest;
import com.server.userservice.domain.dto.request.RegisterRequest;
import com.server.userservice.domain.dto.response.AuthResponse;
import com.server.userservice.domain.dto.response.ValidateTokenResponse;

public interface UserService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    ValidateTokenResponse validateToken(String token);
}
