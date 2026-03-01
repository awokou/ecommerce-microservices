package com.server.orderservice.service;

import com.server.orderservice.domain.dto.request.OrderLineRequest;
import com.server.orderservice.domain.dto.response.OrderLineResponse;

import java.util.List;

public interface OrderLineService {
    List<OrderLineResponse> findAllOrderLinesByOrderId(Long orderId);

    Long saveOrderLine(OrderLineRequest orderLineRequest);
}
