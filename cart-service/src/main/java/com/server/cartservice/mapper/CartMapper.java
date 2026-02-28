package com.server.cartservice.mapper;

import com.server.cartservice.domain.dto.response.CartLineResponse;
import com.server.cartservice.domain.dto.response.CartResponse;
import com.server.cartservice.domain.entity.Cart;
import com.server.cartservice.domain.entity.CartLine;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CartMapper {

    public CartResponse mapToResponse(Cart cart) {
        if (cart == null) {
            return null;
        }
        List<CartLineResponse> cartLineResponses =
                cart.getCartLines() == null
                        ? Collections.emptyList()
                        : cart.getCartLines()
                        .stream()
                        .map(this::mapLineToResponse)
                        .toList();

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .cartLineResponses(cartLineResponses)
                .subtotal(cart.getSubtotal())
                .total(cart.getTotal())
                .totalItems(cart.getTotalLines())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
    public CartLineResponse mapLineToResponse(CartLine line) {
        if (line == null) {
            return null;
        }
        return CartLineResponse.builder()
                .id(line.getId())
                .productCode(line.getProductCode())
                .name(line.getName())
                .imageUrl(line.getImageUrl())
                .quantity(line.getQuantity())
                .unitPrice(line.getUnitPrice())
                .lineTotal(line.getLineTotal())
                .available(line.isAvailable())
                .build();
    }
}
