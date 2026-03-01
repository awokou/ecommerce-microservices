package com.server.orderservice.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineResponse {
    private Long id;
    private String productCode;
    private String name;
    private String imageUrl;
    private int quantity;
    private BigDecimal unitPrice;
}
