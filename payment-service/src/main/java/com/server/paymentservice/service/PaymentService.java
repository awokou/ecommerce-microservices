package com.server.paymentservice.service;

import java.util.List;

import com.server.paymentservice.domain.dto.request.PaymentRequest;
import com.server.paymentservice.domain.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentRequest createPayment(PaymentRequest paymentRequest);

    List<PaymentResponse> getAllPayments();

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse getPaymentByOrderId(Long orderId);
}
