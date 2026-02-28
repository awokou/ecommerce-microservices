package com.server.notificationservice.domain.dto;

import lombok.Builder;

@Builder
public record RecipientInfo(
        String email,
        String name

) {
}
