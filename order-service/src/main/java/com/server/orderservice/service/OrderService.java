package com.server.orderservice.service;

import com.server.orderservice.domain.dto.request.OrderLineRequest;
import com.server.orderservice.domain.dto.request.OrderRequest;
import com.server.orderservice.domain.dto.response.OrderLineResponse;
import com.server.orderservice.domain.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    List<OrderResponse> findAllOrders();

    OrderResponse findOrder(Long orderId);

    Long createdOrder(OrderRequest request);

    List<OrderLineResponse> findAllOrderLinesByOrderId(Long orderId);

    Long saveOrderLine(OrderLineRequest orderLineRequest);
}
