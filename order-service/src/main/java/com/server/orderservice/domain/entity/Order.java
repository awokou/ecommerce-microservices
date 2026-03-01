package com.server.orderservice.domain.entity;

import com.server.orderservice.domain.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    private String userId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<OrderLine> orderLines = new ArrayList<>();

    public void increaseTotalPrice(BigDecimal value) {
        setTotalPrice(this.totalPrice.add(value));
    }

    public void addItem(OrderLine orderItem) {
        orderLines.add(orderItem);
    }

    public void removeItem(Long itemId) {
        this.orderLines.removeIf(item -> item.getId().equals(itemId));
    }
}
