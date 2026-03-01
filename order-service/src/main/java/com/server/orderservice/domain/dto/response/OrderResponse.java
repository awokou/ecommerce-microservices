package com.server.orderservice.domain.dto.response;

import com.server.orderservice.domain.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
        private Long id;
        private String orderNumber;
        private String userId;
        private PaymentMethod paymentMethod;
        private BigDecimal totalPrice;
}
