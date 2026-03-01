package com.server.paymentservice.domain.dto;

public record UserResponse(
        Long id,
        String name,
        String email) {
}
