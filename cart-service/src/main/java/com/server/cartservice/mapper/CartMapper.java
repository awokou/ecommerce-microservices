package com.server.cartservice.mapper;

import com.server.cartservice.domain.dto.response.CartItemResponse;
import com.server.cartservice.domain.dto.response.CartResponse;
import com.server.cartservice.domain.entity.Cart;
import com.server.cartservice.domain.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CartMapper {

    public CartResponse mapToResponse(Cart cart) {
        if (cart == null) {
            return null;
        }
        List<CartItemResponse> itemResponses =
                cart.getItems() == null
                        ? Collections.emptyList()
                        : cart.getItems()
                        .stream()
                        .map(this::mapItemToResponse)
                        .toList();

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(itemResponses)
                .subtotal(cart.getSubtotal())
                .total(cart.getTotal())
                .totalItems(cart.getTotalItems())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
    public CartItemResponse mapItemToResponse(CartItem item) {
        if (item == null) {
            return null;
        }
        return CartItemResponse.builder()
                .id(item.getId())
                .productCode(item.getProductCode())
                .name(item.getName())
                .imageUrl(item.getImageUrl())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .available(item.isAvailable())
                .build();
    }
}
