package com.server.orderservice.domain.dto.request;

import java.math.BigDecimal;

public record OrderLineRequest(Long orderId,
                               String productCode,
                               String name,
                               String imageUrl,
                               int quantity,
                               BigDecimal unitPrice) {
}
