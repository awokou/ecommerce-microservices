package com.server.userservice.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String imageUrl;
    private String email;
    private String phone;
    private String role;
    private String token;
    private long expiresIn;
}