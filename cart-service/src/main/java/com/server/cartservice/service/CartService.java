package com.server.cartservice.service;

import com.server.cartservice.domain.dto.request.AddLineRequest;
import com.server.cartservice.domain.dto.request.UpdateQuantityRequest;
import com.server.cartservice.domain.dto.response.CartResponse;

public interface CartService {

    CartResponse createCart(String userId);

    CartResponse getCart(String cartId);

    CartResponse addLine(String cartId, AddLineRequest request);

    CartResponse updateLineQuantity(String cartId, String productCode, UpdateQuantityRequest request);

    CartResponse removeLine(String cartId, String productCode);

    void clearCart(String cartId);

    void deleteCart(String cartId);
}
