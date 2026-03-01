package com.server.orderservice.domain.dto.response;

import java.math.BigDecimal;

public record PurchaseResponse(Long id,
         String productCode,
         String name,
         String description,
         BigDecimal price,
         String imageUrl,
         String category,
         Integer stockQuantity,
         Boolean available) {
}
