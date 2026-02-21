package com.server.cartservice.service;

import com.server.cartservice.dto.request.AddItemRequest;
import com.server.cartservice.dto.request.UpdateQuantityRequest;
import com.server.cartservice.dto.response.CartResponse;

public interface CartService {

    CartResponse createCart(String userId);

    CartResponse getCart(String cartId);

    CartResponse addItem(String cartId, AddItemRequest request);

    CartResponse updateItemQuantity(String cartId, String code, UpdateQuantityRequest request);

    CartResponse removeItem(String cartId, String code);

    void clearCart(String cartId);

    void deleteCart(String cartId);
}
