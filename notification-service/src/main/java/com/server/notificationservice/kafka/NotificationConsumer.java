package com.server.notificationservice.kafka;

import com.server.notificationservice.domain.dto.OrderConfirmation;
import com.server.notificationservice.domain.dto.PaymentConfirmation;
import com.server.notificationservice.domain.dto.Product;
import com.server.notificationservice.domain.entity.Notification;
import com.server.notificationservice.domain.enums.NotificationType;
import com.server.notificationservice.repository.NotificationRepository;
import com.server.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic", groupId = "payment-notification")
    public void consumePaymentConfirmationNotification(PaymentConfirmation paymentConfirmation) {
        log.info("Received message from payment-topic: {}", paymentConfirmation);
        Notification notification = Notification.builder()
                .type(NotificationType.PAYMENT_CONFIRMATION)
                .notificationDate(LocalDateTime.now())
                .paymentConfirmation(paymentConfirmation)
                .build();

        notificationRepository.save(notification);

        String destinationEmail = paymentConfirmation.getEmail();
        String customerName = paymentConfirmation.getFirstName() + " " + paymentConfirmation.getLastName();
        BigDecimal amount = paymentConfirmation.getAmount();
        String orderReference = paymentConfirmation.getOrderNumber();

        emailService.sendPaymentSuccessEmail(destinationEmail, customerName, amount, orderReference);
    }

    @KafkaListener(topics = "order-topic", groupId = "order-notification")
    public void consumeOrderConfirmationNotification(OrderConfirmation orderConfirmation) {
        log.info("Received message from order-topic: {}", orderConfirmation);
        Notification notification = Notification.builder()
                .type(NotificationType.ORDER_CONFIRMATION)
                .notificationDate(LocalDateTime.now())
                .orderConfirmation(orderConfirmation)
                .build();

        notificationRepository.save(notification);

        String destinationEmail = orderConfirmation.getUser().getEmail();
        String customerName = orderConfirmation.getUser().getFirstName() + " "
                + orderConfirmation.getUser().getLastName();
        BigDecimal totalAmount = orderConfirmation.getTotalAmount();
        String orderReference = orderConfirmation.getOrderNumber();
        List<Product> products = orderConfirmation.getProducts();

        emailService.sendOrderConfirmationEmail(destinationEmail, customerName, totalAmount, orderReference, products);
    }
}
