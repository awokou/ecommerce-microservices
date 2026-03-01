package com.server.orderservice.domain.dto.response;

public record UserResponse(
        Long id,
        String name,
        String email) {
}
