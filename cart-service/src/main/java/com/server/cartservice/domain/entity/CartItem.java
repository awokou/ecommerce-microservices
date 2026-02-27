package com.server.cartservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItem implements Serializable {

    @Id
    private String id;
    private String productCode;
    private String name;
    private String imageUrl;
    private int quantity;
    private BigDecimal unitPrice;

    private boolean available;

    public BigDecimal getLineTotal() {
        if (unitPrice == null || quantity <= 0) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public static String generateItemId() {
        return "ITEM-" + UUID.randomUUID();
    }
}