package com.server.orderservice.domain.mapper;

import com.server.orderservice.domain.dto.request.OrderLineRequest;
import com.server.orderservice.domain.dto.response.OrderLineResponse;
import com.server.orderservice.domain.entity.Order;
import com.server.orderservice.domain.entity.OrderLine;
import org.springframework.stereotype.Component;

@Component
public class OrderLineMapper {

    public OrderLine toOrderLine(OrderLineRequest orderLineRequest) {
        return OrderLine.builder()
                .productCode(orderLineRequest.productCode())
                .quantity(orderLineRequest.quantity())
                .order(Order.builder().id(orderLineRequest.orderId()).build())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return OrderLineResponse.builder()
                .id(orderLine.getId())
                .quantity(orderLine.getQuantity())
                .build();
    }
}
