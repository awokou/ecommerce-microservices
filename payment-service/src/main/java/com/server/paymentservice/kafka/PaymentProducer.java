package com.server.paymentservice.kafka;

import com.server.paymentservice.config.KafkaPaymentTopicConfig;
import com.server.paymentservice.domain.event.PaymentNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentProducer {

    public final KafkaTemplate<String, PaymentNotification> kafkaTemplate;

    public void sendNotification(PaymentNotification paymentNotification) {
        log.info("Sending notification with the following information: {}", paymentNotification);
        Message<PaymentNotification> message = MessageBuilder
                .withPayload(paymentNotification)
                .setHeader(KafkaHeaders.TOPIC, KafkaPaymentTopicConfig.PAYMENT_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }
}
