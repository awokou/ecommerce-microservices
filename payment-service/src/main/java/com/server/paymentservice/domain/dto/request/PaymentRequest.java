package com.server.paymentservice.domain.dto.request;

import com.server.paymentservice.domain.dto.response.UserResponse;
import com.server.paymentservice.domain.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    private String orderNumber;

    @Valid
    @NotNull(message = "User information must be provided")
    private UserResponse userResponse;
}
