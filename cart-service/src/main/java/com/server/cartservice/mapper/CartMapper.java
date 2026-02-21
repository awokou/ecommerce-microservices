package com.server.cartservice.mapper;

import com.server.cartservice.dto.response.CartItemResponse;
import com.server.cartservice.dto.response.CartResponse;
import com.server.cartservice.entity.Cart;
import com.server.cartservice.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUserId())
                .items(itemResponses)
                .totalItems(cart.getTotalItems())
                .subtotal(cart.getSubtotal())
                .total(cart.getTotal())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
    public CartItemResponse mapItemToResponse(CartItem item) {
        return CartItemResponse.builder()
                .itemId(item.getItemId())
                .productId(item.getProductCode())
                .productName(item.getProductName())
                .productImage(item.getProductImage())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .available(item.isAvailable())
                .build();
    }
}
