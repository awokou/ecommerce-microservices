package com.server.notificationservice.domain.dto;

import com.server.notificationservice.domain.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfirmation {
    private String orderNumber;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private User user;
    private List<Product> products;
}
