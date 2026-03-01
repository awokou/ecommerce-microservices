package com.server.orderservice.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_lines")
public class OrderLine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productCode;
    private String name;
    private String imageUrl;

    @Column(name = "quantity", columnDefinition = "Integer default 0", nullable = false)
    private int quantity;

    @Column(name = "unit_price", columnDefinition = "Decimal(10,2) default '0.00'", nullable = false)
    private BigDecimal unitPrice = BigDecimal.valueOf(0);

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
