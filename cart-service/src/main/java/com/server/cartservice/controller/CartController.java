package com.server.cartservice.controller;

import com.server.cartservice.domain.dto.request.AddItemRequest;
import com.server.cartservice.domain.dto.request.UpdateQuantityRequest;
import com.server.cartservice.domain.dto.response.CartResponse;
import com.server.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> createCart(@RequestParam(required = false) String userId) {
        log.info("REST request to create cart for user: {}", userId);
        CartResponse cart = cartService.createCart(userId);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartResponse> addItem(@PathVariable String cartId, @Valid @RequestBody AddItemRequest request) {
        log.info("REST request to add item to cart {}: {}", cartId, request);
        CartResponse cart = cartService.addItem(cartId, request);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String cartId) {
        log.info("REST request to get cart: {}", cartId);
        CartResponse cart = cartService.getCart(cartId);
        return ResponseEntity.ok(cart);
    }


    @PutMapping("/{cartId}/items/{productCode}")
    public ResponseEntity<CartResponse> updateItemQuantity(@PathVariable String cartId, @PathVariable String productCode, @Valid @RequestBody UpdateQuantityRequest request) {
        log.info("REST request to update quantity in cart {}: productCode={}, quantity={}", cartId, productCode, request.getQuantity());
        CartResponse cart = cartService.updateItemQuantity(cartId, productCode, request);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}/items/{productCode}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable String cartId, @PathVariable String productCode) {
        log.info("REST request to remove item from cart {}: code={}", cartId, productCode);
        CartResponse cart = cartService.removeItem(cartId, productCode);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable String cartId) {
        log.info("REST request to clear cart: {}", cartId);
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable String cartId) {
        log.info("REST request to delete cart: {}", cartId);
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}