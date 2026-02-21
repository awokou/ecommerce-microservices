package com.server.cartservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    private String itemId;
    private String productCode;
    private String productName;
    private String productImage;
    private int quantity;
    private BigDecimal unitPrice;

    private boolean available;

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    public static String generateItemId() {
        return "ITEM-" + UUID.randomUUID().toString();
    }
}