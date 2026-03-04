package com.server.paymentservice.service;

import com.server.paymentservice.domain.dto.request.PaymentRequest;

public interface PaymentService {
    Long createPayment(PaymentRequest paymentRequest);
}
