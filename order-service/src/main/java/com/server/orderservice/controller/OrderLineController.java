package com.server.orderservice.controller;


import com.server.orderservice.domain.dto.response.OrderLineResponse;
import com.server.orderservice.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-lines")
@RequiredArgsConstructor
public class OrderLineController {

    private final OrderLineService service;

    @GetMapping(path = "/order/{orderId}")
    public ResponseEntity<List<OrderLineResponse>> findOrderLinesByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(this.service.findAllOrderLinesByOrderId(orderId));
    }
}
