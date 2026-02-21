package com.server.cartservice.client;

import com.server.cartservice.dto.external.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public ProductDto getProduct(String code) {
        log.warn("Fallback: Catalog Service unavailable for product: {}", code);

        return ProductDto.builder()
                .code(code)
                .name("Product temporarily unavailable")
                .description("Please try again later")
                .price(BigDecimal.ZERO)
                .available(false)
                .stockQuantity(0)
                .build();
    }

    @Override
    public Boolean checkAvailability(String productId, int quantity) {
        log.warn("Fallback: Catalog Service unavailable for availability check: {}", productId);
        return false;
    }
}
