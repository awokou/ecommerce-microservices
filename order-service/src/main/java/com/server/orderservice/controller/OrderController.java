package com.server.orderservice.controller;

import com.server.orderservice.domain.dto.request.OrderRequest;
import com.server.orderservice.domain.dto.response.OrderResponse;
import com.server.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAllOrders() {
        return ResponseEntity.ok(this.orderService.findAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(this.orderService.findOrder(orderId));
    }

    @PostMapping
    public ResponseEntity<Long> createOrder(@Valid @RequestBody OrderRequest request) {
        Long orderId = this.orderService.createdOrder(request);
        URI uriOrder = URI.create("/api/v1/orders/" + orderId);
        return ResponseEntity.created(uriOrder).body(orderId);
    }
}
