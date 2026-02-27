package com.server.cartservice.client;

import com.server.cartservice.domain.dto.external.ProductDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "catalog-service",fallback = ProductClientFallback.class)
public interface ProductClient {

    @GetMapping("/api/v1/products/{productCode}")
    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
    @Retry(name = "productService")
    ProductDto getProduct(@PathVariable("productCode") String productCode);

    @GetMapping("/api/v1/products/{productCode}/availability")
    @CircuitBreaker(name = "productService")
    @Retry(name = "productService")
    Boolean checkAvailability(@PathVariable("productCode") String productCode, @RequestParam("quantity") int quantity);
}
