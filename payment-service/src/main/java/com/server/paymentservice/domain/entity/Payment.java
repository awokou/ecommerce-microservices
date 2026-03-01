package com.server.paymentservice.domain.entity;

import com.server.paymentservice.domain.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;

    private Long orderId;
    private String userId;

    private Boolean isPayed;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
}
