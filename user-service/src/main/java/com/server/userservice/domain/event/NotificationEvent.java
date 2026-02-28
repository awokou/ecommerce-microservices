package com.server.userservice.domain.event;

import com.server.userservice.domain.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {
    String channel;
    String recipient;
    String templateCode;
    Map<String, Object> params;
    NotificationType notificationType;
}
