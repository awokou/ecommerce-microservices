package com.server.cartservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")
public class Cart implements Serializable {

    @Id
    private String cartId;

    private String userId;

    @ElementCollection
    @CollectionTable(
            name = "cart_items",
            joinColumns = @JoinColumn(name = "cart_id")
    )
    private List<CartItem> items = new ArrayList<>();

    private BigDecimal totalPrice;
    private BigDecimal subtotal;
    private BigDecimal total;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;

    public void addItem(CartItem newItem) {
        CartItem existingItem = items.stream()
                .filter(item -> item.getCode().equals(newItem.getCode()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + newItem.getQuantity();
            existingItem.setQuantity(newQuantity);
        } else {
            this.items.add(newItem);
        }
        this.updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    public void removeItem(String productId) {
        this.items.removeIf(item -> item.getCode().equals(productId));
        this.updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    public void updateItemQuantity(String productId, int quantity) {
        CartItem updatedItem =  items.stream()
                .filter(item -> item.getCode().equals(productId))
                .findFirst()
                .orElse(null);
        if (updatedItem != null) {
            updatedItem.setQuantity(quantity);
            this.updatedAt = LocalDateTime.now();
            calculateTotals();
        }
    }

    public void clear() {
        this.items.clear();
        this.updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    public void calculateTotals() {
        BigDecimal cumul = BigDecimal.ZERO;

        if (items != null) {
            for (CartItem item : items) {
                BigDecimal montantLigne = item.getLineTotal();
                cumul = cumul.add(montantLigne);
            }
        }
        this.subtotal = cumul;
        this.totalPrice = this.subtotal;
    }

    public int getTotalItems() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public static String generateCartId() {
        return "CART-" + UUID.randomUUID().toString();
    }
}
