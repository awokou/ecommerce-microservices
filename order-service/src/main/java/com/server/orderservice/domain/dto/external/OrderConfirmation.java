package com.server.orderservice.domain.dto.external;

import com.server.orderservice.domain.dto.response.PurchaseResponse;
import com.server.orderservice.domain.dto.response.UserResponse;
import com.server.orderservice.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(String orderNumber,
                                BigDecimal totalPrice,
                                PaymentMethod paymentMethod,
                                UserResponse user,
                                List<PurchaseResponse> products) {
}
