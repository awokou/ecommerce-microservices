package com.server.orderservice.service.impl;

import com.server.orderservice.client.ProductClient;
import com.server.orderservice.client.UserClient;
import com.server.orderservice.domain.dto.request.OrderLineRequest;
import com.server.orderservice.domain.dto.request.OrderRequest;
import com.server.orderservice.domain.dto.response.OrderLineResponse;
import com.server.orderservice.domain.dto.response.OrderResponse;
import com.server.orderservice.domain.dto.response.UserResponse;
import com.server.orderservice.domain.entity.OrderLine;
import com.server.orderservice.domain.mapper.OrderLineMapper;
import com.server.orderservice.domain.mapper.OrderMapper;
import com.server.orderservice.exception.OrderNotFoundException;
import com.server.orderservice.kafka.OrderProducer;
import com.server.orderservice.repository.OrderLineRepository;
import com.server.orderservice.repository.OrderRepository;
import com.server.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final UserClient userClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderProducer orderProducer;
    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;


    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new OrderNotFoundException("No order found with the provided id: " + orderId));
    }

    @Override
    @Transactional
    public Long createdOrder(OrderRequest request) {
        UserResponse userResponse = userClient.findUser(request.userId())
                .orElseThrow(() -> new OrderNotFoundException(String.format("No se puede crear la orden. El cliente con id %s no existe", request.userId())));

        return 0L;
    }

     @Override
    public List<OrderLineResponse> findAllOrderLinesByOrderId(Long orderId) {
        return this.orderLineRepository.findAllByOrderId(orderId).stream()
                .map(this.orderLineMapper::toOrderLineResponse)
                .toList();
    }

    @Override
    public Long saveOrderLine(OrderLineRequest orderLineRequest) {
        OrderLine orderLine = this.orderLineMapper.toOrderLine(orderLineRequest);
        return this.orderLineRepository.save(orderLine).getId();
    }
}
