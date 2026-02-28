package com.server.cartservice.domain.dto.response;

public record UserResponse(
        Long id,
        String name,
        String email) {
}
