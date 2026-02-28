package com.server.notificationservice.domain.entity;


import com.server.notificationservice.domain.dto.RecipientInfo;
import com.server.notificationservice.domain.enums.NotificationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    private String id;
    private NotificationType type;
    private String title;
    private String content;
    private LocalDateTime notificationDate;
    private Map<String, Object> details;
    private RecipientInfo recipientInfo;
}
