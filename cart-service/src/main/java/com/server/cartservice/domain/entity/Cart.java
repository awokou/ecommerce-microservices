package com.server.cartservice.domain.entity;

import com.server.cartservice.domain.enums.CartStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private BigDecimal totalPrice;
    private BigDecimal subtotal;
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private List<CartLine> cartLines = new ArrayList<>();


    public void addLine(CartLine newLine) {
        CartLine existingItem = cartLines.stream()
                .filter(item -> item.getProductCode().equals(newLine.getProductCode()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + newLine.getQuantity();
            existingItem.setQuantity(newQuantity);
        } else {
            this.cartLines.add(newLine);
        }
        this.updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    public void removeLine(String productCode) {
        this.cartLines.removeIf(item -> item.getProductCode().equals(productCode));
        this.updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    public void updateLineQuantity(String productCode, int quantity) {
        CartLine updatedLine =  cartLines.stream()
                .filter(item -> item.getProductCode().equals(productCode))
                .findFirst()
                .orElse(null);
        if (updatedLine != null) {
            updatedLine.setQuantity(quantity);
            this.updatedAt = LocalDateTime.now();
            calculateTotals();
        }
    }

    public void clear() {
        this.cartLines.clear();
        this.updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    public void calculateTotals() {
        BigDecimal cumul = BigDecimal.ZERO;

        if (cartLines != null) {
            for (CartLine line : cartLines) {
                BigDecimal montantLigne = line.getLineTotal();
                cumul = cumul.add(montantLigne);
            }
        }
        this.subtotal = cumul;
        this.totalPrice = this.subtotal;
    }

    public int getTotalLines() {
        return cartLines.stream()
                .mapToInt(CartLine::getQuantity)
                .sum();
    }
}
