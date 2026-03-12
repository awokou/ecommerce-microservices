package com.server.userservice.service;

import com.server.userservice.domain.dto.external.LoginDto;
import com.server.userservice.domain.dto.external.ResendEmailConfirmationDto;
import com.server.userservice.domain.dto.external.ResetPasswordDto;
import com.server.userservice.domain.dto.response.AuthResponse;
import com.server.userservice.domain.dto.response.ValidateTokenResponse;

public interface AuthService {

    AuthResponse login(LoginDto request);

    void verifyEmail(String token);

    void forgotPassword(String email);

    void resetPassword(ResetPasswordDto reset);

    void resendEmailConfirmation(ResendEmailConfirmationDto request);

    ValidateTokenResponse validateToken(String token);
}
