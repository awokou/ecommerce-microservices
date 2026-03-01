package com.server.orderservice.service.impl;

import com.server.orderservice.domain.dto.request.OrderLineRequest;
import com.server.orderservice.domain.dto.response.OrderLineResponse;
import com.server.orderservice.domain.entity.OrderLine;
import com.server.orderservice.domain.mapper.OrderLineMapper;
import com.server.orderservice.repository.OrderLineRepository;
import com.server.orderservice.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;

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
