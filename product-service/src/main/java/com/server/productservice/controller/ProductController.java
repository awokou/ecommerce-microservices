package com.server.productservice.controller;

import com.server.productservice.domain.dto.request.ProductRequest;
import com.server.productservice.domain.dto.response.ProductResponse;
import com.server.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<ProductRequest> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("Rest request to create product: {}", request.getProductCode());
        ProductRequest product = productService.createProduct(request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{productCode}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String productCode) {
        log.info("Rest request to get product: {}", productCode);
        ProductResponse product = productService.getProduct(productCode);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Get all products by name")
    @GetMapping("/{productName}")
    public ResponseEntity<List<ProductResponse>> getAllProductsByName(@PathVariable String productName) {
        log.info("Rest request to get all products by name: {}", productName);
        List<ProductResponse> product = productService.getAllProductsByName(productName);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Get all products with available true")
    @GetMapping("/available")
    public ResponseEntity<List<ProductResponse>> getAllAvailableTrue() {
        log.info("Rest request to get all products with available true");
        List<ProductResponse> product = productService.getAllAvailableTrue();
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Get all products")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("Rest request to get all products");
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get products by categoryId")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        log.info("Rest request to get products by categoryId: {}", categoryId);
        List<ProductResponse> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Check product availability")
    @GetMapping("/{productCode}/availability")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable String productCode, @RequestParam int quantity) {
        log.info("Rest request to check availability: productCode={}, quantity={}", productCode, quantity);
        boolean available = productService.isProductAvailable(productCode, quantity);
        return ResponseEntity.ok(available);
    }

    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    public ResponseEntity<ProductRequest> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        log.info("Rest request to update product: {}", id);
        ProductRequest product = productService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        log.info("Rest request to delete product: {}", id);
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}