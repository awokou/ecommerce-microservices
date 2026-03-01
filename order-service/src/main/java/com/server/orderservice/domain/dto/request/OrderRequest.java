package com.server.orderservice.domain.dto.request;

import com.server.orderservice.domain.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        String orderNumber,

        @NotNull(message = "The amount must not be null")
        @Positive(message = "The amount must be positive")
        BigDecimal totalPrice,

        @NotNull(message = "The payment method must not be null")
        PaymentMethod paymentMethod,

        @NotBlank(message = "The users must be present")
        Long userId,

        @NotEmpty(message = "You must purchase at least one product")
        List<@Valid PurchaseRequest> products
) {
}
