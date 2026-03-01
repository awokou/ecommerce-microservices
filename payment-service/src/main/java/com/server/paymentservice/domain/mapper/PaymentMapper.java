package com.server.paymentservice.domain.mapper;

import com.server.paymentservice.domain.dto.PaymentRequest;
import com.server.paymentservice.domain.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public Payment toPayment(PaymentRequest request) {
        return Payment.builder()
                .amount(request.amount())
                .paymentMethod(request.paymentMethod())
                .orderId(request.orderId())
                .build();
    }
}
