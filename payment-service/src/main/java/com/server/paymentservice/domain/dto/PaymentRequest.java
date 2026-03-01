package com.server.paymentservice.domain.dto;

import com.server.paymentservice.domain.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest(

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        @NotNull(message = "Order ID is required")
        Long orderId,

        String orderReference,

        @Valid
        @NotNull(message = "User information must be provided")
        UserResponse userResponse
) {
}
