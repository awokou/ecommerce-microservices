package com.server.notificationservice.domain.dto;

import com.server.notificationservice.domain.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmation {
    private String orderNumber;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String name;
    private String email;

}
