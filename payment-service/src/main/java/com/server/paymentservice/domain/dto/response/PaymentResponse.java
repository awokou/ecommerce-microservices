package com.server.paymentservice.domain.dto.response;

import java.math.BigDecimal;

import com.server.paymentservice.domain.enums.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String transactionId;
    private PaymentMethod paymentMethod;
    private Boolean isPayed;
}
