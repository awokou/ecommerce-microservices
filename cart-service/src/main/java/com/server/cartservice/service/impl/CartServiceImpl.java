package com.server.cartservice.service.impl;

import com.server.cartservice.client.ProductClient;
import com.server.cartservice.domain.dto.external.ProductDto;
import com.server.cartservice.domain.dto.request.AddItemRequest;
import com.server.cartservice.domain.dto.request.UpdateQuantityRequest;
import com.server.cartservice.domain.dto.response.CartResponse;
import com.server.cartservice.domain.entity.Cart;
import com.server.cartservice.domain.entity.CartItem;
import com.server.cartservice.exception.CartNotFoundException;
import com.server.cartservice.exception.InvalidCartOperationException;
import com.server.cartservice.mapper.CartMapper;
import com.server.cartservice.repository.CartRepository;
import com.server.cartservice.service.CartService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    @Value("${cart.max-items}")
    private int maxItems;

    @Value("${cart.ttl-days}")
    private long timeBeforeCartToExpire;

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductClient productClient;

    @Override
    @Transactional
    public CartResponse createCart(String userId) {
        log.info("Creating new Cart for user : {}", userId);
        Cart cart = Cart.builder()
                .id(Cart.generateCartId())
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(timeBeforeCartToExpire))
                .build();
        cart.calculateTotals();
        Cart savedCart = cartRepository.save(cart);
        log.info("Cart created..!!:{}", savedCart.getCreatedAt());

        return cartMapper.mapToResponse(savedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(String cartId) {
        log.info("Retrieving cart: {}", cartId);
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found: " + cartId));

        return cartMapper.mapToResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(String cartId, AddItemRequest request) {
        log.info("Adding item to cart {}: code={}, quantity={}", cartId, request.getProductCode(), request.getQuantity());

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found: " + cartId));

        ProductDto product = validateProduct(request.getProductCode());

        boolean isAvailable = validateAvailability(request.getProductCode(), request.getQuantity());
        if (!isAvailable) {
            throw new InvalidCartOperationException(
                    "Product " + request.getProductCode() + " is not available in requested quantity");
        }

        if (cart.getTotalItems() + request.getQuantity() > maxItems) {
            throw new InvalidCartOperationException("Cannot add item. Cart limit of " + maxItems);
        }

        CartItem cartItem = CartItem.builder()
                .id(CartItem.generateItemId())
                .productCode(product.getProductCode())
                .name(product.getName())
                .imageUrl(product.getImageUrl())
                .quantity(request.getQuantity())
                .unitPrice(product.getPrice())
                .available(true)
                .build();

        cart.addItem(cartItem);

        Cart savedCart = cartRepository.save(cart);

        log.info("Item added to cart....!! {}", cartId);

        return cartMapper.mapToResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponse updateItemQuantity(String cartId, String productCode, UpdateQuantityRequest request) {
        log.info("Updating item quantity in cart {}: productCode={}, newQuantity={}", cartId, productCode, request.getQuantity());

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found: " + cartId));

        // Validation via Catalog
        boolean isAvailable = validateAvailability(productCode, request.getQuantity());
        if (!isAvailable) {
            throw new InvalidCartOperationException("Product not available in requested quantity");
        }

        boolean productExists = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProductCode().equals(productCode)) {
                productExists = true;
                break;
            }
        }
        if (!productExists) {
            throw new InvalidCartOperationException("Product not found in cart: " + productCode);
        }

        cart.updateItemQuantity(productCode, request.getQuantity());

        Cart savedCart = cartRepository.save(cart);

        log.info("Item quantity updated...!!! in cart {}", cartId);

        return cartMapper.mapToResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponse removeItem(String cartId, String productCode) {

        log.info("Removing item from cart {}: productCode={}", cartId, productCode);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found: " + cartId));

        cart.removeItem(productCode);

        Cart savedCart = cartRepository.save(cart);

        log.info("Item removed successfully from cart {}", cartId);

        return cartMapper.mapToResponse(savedCart);
    }

    @Override
    @Transactional
    public void clearCart(String cartId) {
        log.info("Clearing cart: {}", cartId);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found: " + cartId));

        cart.clear();
        cartRepository.save(cart);

        log.info("Cart cleared successfully: {}", cartId);
    }

    @Override
    @Transactional
    public void deleteCart(String cartId) {
        log.info("Deleting cart: {}", cartId);
        if (!cartRepository.existsById(cartId)) {
            throw new CartNotFoundException("Cart not found: " + cartId);
        }
        cartRepository.deleteById(cartId);
        log.info("Cart deleted successfully: {}", cartId);
    }

    private ProductDto validateProduct(String productCode) {
        try {
            log.debug("Validating product via Catalog Service: {}", productCode);
            ProductDto product = productClient.getProduct(productCode);
            log.debug("Product validated: {}", product.getName());
            return product;
        } catch (FeignException.NotFound e) {
            log.error("Product not found in catalog: {}", productCode);
            throw new InvalidCartOperationException("Product not found: " + productCode);
        } catch (FeignException e) {
            log.error("Error communicating with Catalog Service: {}", e.getMessage());
            throw new InvalidCartOperationException(
                    "Unable to validate product. Please try again later.");
        }
    }

    private boolean validateAvailability(String productCode, int quantity) {
        try {
            log.debug("Checking availability: productCode={}, quantity={}", productCode, quantity);
            return productClient.checkAvailability(productCode, quantity);
        } catch (FeignException e) {
            log.error("Error checking availability: {}", e.getMessage());
            // En cas d'erreur, on refuse l'ajout (fail-safe)
            return false;
        }
    }
}
