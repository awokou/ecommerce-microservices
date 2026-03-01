package com.server.paymentservice.service;

import com.server.paymentservice.domain.dto.PaymentRequest;

public interface PaymentService {
    Long createPayment(PaymentRequest paymentRequest);
}
