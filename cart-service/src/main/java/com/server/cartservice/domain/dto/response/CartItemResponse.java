package com.server.cartservice.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private String id;
    private String productCode;
    private String name;
    private String imageUrl;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private boolean available;
}
