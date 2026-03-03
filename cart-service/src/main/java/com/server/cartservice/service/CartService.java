package com.server.cartservice.service;

import com.server.cartservice.domain.dto.request.AddLineRequest;
import com.server.cartservice.domain.dto.request.UpdateQuantityRequest;
import com.server.cartservice.domain.dto.response.CartResponse;

public interface CartService {

    CartResponse createCart(Long userId);

    CartResponse getCart(Long cartId);

    CartResponse addLine(Long cartId, AddLineRequest request);

    CartResponse updateLineQuantity(Long cartId, String productCode, UpdateQuantityRequest request);

    CartResponse removeLine(Long cartId, String productCode);

    void clearCart(Long cartId);

    void deleteCart(Long cartId);
}
