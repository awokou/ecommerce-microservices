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
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("Rest request to create product: {}", request.getCode());
        ProductResponse product = productService.createProduct(request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{code}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String code) {
        log.info("Rest request to get product: {}", code);
        ProductResponse product = productService.getProduct(code);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Get all products by name")
    @GetMapping("/{name}")
    public ResponseEntity<List<ProductResponse>> getAllProductsByName(@PathVariable String name) {
        log.info("Rest request to get all products by name: {}", name);
        List<ProductResponse> product = productService.getAllProductsByName(name);
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

    @Operation(summary = "Get products by category")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        log.info("Rest request to get products by category: {}", category);
        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Check product availability")
    @GetMapping("/{code}/availability")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable String code, @RequestParam int quantity) {
        log.info("Rest request to check availability: code={}, quantity={}", code, quantity);
        boolean available = productService.isProductAvailable(code, quantity);
        return ResponseEntity.ok(available);
    }

    @Operation(summary = "Update product")
    @PutMapping("/{code}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable String code, @Valid @RequestBody ProductRequest request) {
        log.info("Rest request to update product: {}", code);
        ProductResponse product = productService.updateProduct(code, request);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String code) {
        log.info("Rest request to delete product: {}", code);
        productService.deleteProduct(code);
        return ResponseEntity.noContent().build();
    }
}