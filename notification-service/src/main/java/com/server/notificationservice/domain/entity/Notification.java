package com.server.notificationservice.domain.entity;


import com.server.notificationservice.domain.dto.NotificationEvent;
import com.server.notificationservice.domain.dto.OrderConfirmation;
import com.server.notificationservice.domain.dto.PaymentConfirmation;
import com.server.notificationservice.domain.enums.NotificationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    private String id;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private NotificationEvent notificationEvent;
    private OrderConfirmation orderConfirmation;
    private PaymentConfirmation paymentConfirmation;
}
