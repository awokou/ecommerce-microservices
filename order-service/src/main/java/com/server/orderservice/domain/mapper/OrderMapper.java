package com.server.orderservice.domain.mapper;

import com.server.orderservice.domain.dto.request.OrderRequest;
import com.server.orderservice.domain.dto.response.OrderResponse;
import com.server.orderservice.domain.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toOrder(OrderRequest request) {
        return Order.builder()
                .orderNumber(request.orderNumber())
                .totalPrice(request.totalPrice())
                .paymentMethod(request.paymentMethod())
                .userId(request.userId())
                .build();
    }

    public OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .totalPrice(order.getTotalPrice())
                .paymentMethod(order.getPaymentMethod())
                .userId(order.getUserId())
                .build();
    }
}
