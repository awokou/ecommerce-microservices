package com.server.paymentservice.service.impl;

import com.server.paymentservice.domain.event.PaymentNotification;
import com.server.paymentservice.domain.dto.PaymentRequest;
import com.server.paymentservice.domain.entity.Payment;
import com.server.paymentservice.domain.mapper.PaymentMapper;
import com.server.paymentservice.kafka.PaymentProducer;
import com.server.paymentservice.repository.PaymentRepository;
import com.server.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentProducer paymentProducer;

    @Override
    @Transactional
    public Long createPayment(PaymentRequest paymentRequest) {
        Payment paymentDB = paymentRepository.save(paymentMapper.toPayment(paymentRequest));
        PaymentNotification paymentNotification =
                new PaymentNotification(
                        paymentRequest.getOrderNumber(),
                        paymentRequest.getAmount(),
                        paymentRequest.getPaymentMethod(),
                        paymentRequest.getUserResponse().getName(),
                        paymentRequest.getUserResponse().getEmail()
                );

        paymentProducer.sendNotification(paymentNotification);

        return paymentDB.getId();
    }
}
