package com.server.paymentservice.service.impl;

import com.server.paymentservice.domain.dto.PaymentNotification;
import com.server.paymentservice.domain.dto.PaymentRequest;
import com.server.paymentservice.domain.entity.Payment;
import com.server.paymentservice.domain.mapper.PaymentMapper;
import com.server.paymentservice.kafka.NotificationProducer;
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
    private final NotificationProducer notificationProducer;

    @Override
    @Transactional
    public Long createPayment(PaymentRequest paymentRequest) {
        Payment paymentDB = paymentRepository.save(paymentMapper.toPayment(paymentRequest));
        PaymentNotification paymentNotification =
                new PaymentNotification(
                        paymentRequest.orderReference(),
                        paymentRequest.amount(),
                        paymentRequest.paymentMethod(),
                        paymentRequest.userResponse().name(),
                        paymentRequest.userResponse().email()
                );

        notificationProducer.sendNotification(paymentNotification);

        return paymentDB.getId();
    }
}
