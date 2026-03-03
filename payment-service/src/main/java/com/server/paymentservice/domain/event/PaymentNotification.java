package com.server.paymentservice.domain.event;

import com.server.paymentservice.domain.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotification {
    private String orderNumber;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String name;
    private String email;
}
