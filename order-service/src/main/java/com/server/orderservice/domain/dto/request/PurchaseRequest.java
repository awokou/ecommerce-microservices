package com.server.orderservice.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PurchaseRequest(
        @NotNull(message = "Product Code Is required")
        String productCode,

        @NotNull(message = "Quantity is required")
        @Min(value = 1)
        Integer quantity) {
}
