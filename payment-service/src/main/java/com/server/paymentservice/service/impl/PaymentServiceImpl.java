package com.server.paymentservice.service.impl;

import com.server.paymentservice.domain.event.PaymentNotification;
import com.server.paymentservice.domain.dto.request.PaymentRequest;
import com.server.paymentservice.domain.dto.response.PaymentResponse;
import com.server.paymentservice.domain.entity.Payment;

import com.server.paymentservice.kafka.PaymentProducer;
import com.server.paymentservice.repository.PaymentRepository;
import com.server.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    @Override
    @Transactional
    public PaymentRequest createPayment(PaymentRequest paymentRequest) {

        Payment payment = Payment.builder()
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .transactionId(UUID.randomUUID().toString())
                .paymentMethod(paymentRequest.getPaymentMethod())
                .isPayed(true)
                .build();

        PaymentNotification paymentNotification = new PaymentNotification(
                paymentRequest.getOrderNumber(),
                paymentRequest.getAmount(),
                paymentRequest.getPaymentMethod());

        Payment savedPayment = paymentRepository.save(payment);

        paymentProducer.sendNotification(paymentNotification);

        return mapToPayment(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToPaymentResponse)
                .toList();
    }

    @Override
    public PaymentResponse getPaymentById(Long paymentId) {
        return null;
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        return null;
    }

    private PaymentRequest mapToPayment(Payment payment) {
        return PaymentRequest.builder()
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .orderId(payment.getOrderId())
                .build();
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        if (payment == null) {
            return null;
        }
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .isPayed(payment.isPayed())
                .paymentMethod(payment.getPaymentMethod())
                .build();
    }
}
