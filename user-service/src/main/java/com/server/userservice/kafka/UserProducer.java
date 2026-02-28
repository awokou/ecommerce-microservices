package com.server.userservice.kafka;

import com.server.userservice.domain.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProducer {

    public static  final String CONFIRM_REGISTRATION_TOPIC = "confirm-registration-user-topic";
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void sendConfirmRegistrationAccount(NotificationEvent notificationEvent) {
        log.info("Sending confirmation registration user");
        Message<NotificationEvent> message = MessageBuilder
                .withPayload(notificationEvent)
                .setHeader(TOPIC, CONFIRM_REGISTRATION_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

    public void sendForgotPasswordEmail(NotificationEvent notificationEvent) {
        log.info("Sending forgot password email");
        Message<NotificationEvent> message = MessageBuilder
                .withPayload(notificationEvent)
                .setHeader(TOPIC, "password-reset-email-topic")
                .build();
        kafkaTemplate.send(message);
    }
}
