package com.server.orderservice.domain.event;

import com.server.orderservice.domain.dto.response.PurchaseResponse;
import com.server.orderservice.domain.dto.response.UserResponse;
import com.server.orderservice.domain.enums.PaymentMethod;
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
    private BigDecimal totalPrice;
    private PaymentMethod paymentMethod;
    private UserResponse user;
    private List<PurchaseResponse> products;

}
