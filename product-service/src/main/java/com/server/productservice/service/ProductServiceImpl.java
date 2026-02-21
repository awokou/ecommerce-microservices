package com.server.productservice.service;

import com.server.productservice.dto.ProductRequest;
import com.server.productservice.dto.ProductResponse;
import com.server.productservice.entity.Product;
import com.server.productservice.exception.ProductNotFoundException;
import com.server.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getCode());

        Product product = Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .category(request.getCategory())
                .stockQuantity(request.getStockQuantity())
                .available(request.getStockQuantity() > 0)
                .build();

        Product saved = productRepository.save(product);
        log.info("Product created successfully: {}", saved.getCode());

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(String code) {
        log.info("Getting product: {}", code);

        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + code));

        return mapToResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Getting all products");

        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Getting products by category: {}", category);

        return productRepository.findByCategory(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(String code, ProductRequest request) {
        log.info("Updating product: {}", code);

        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + code));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(request.getCategory());
        product.setStockQuantity(request.getStockQuantity());
        product.setAvailable(request.getStockQuantity() > 0);

        Product updated = productRepository.save(product);
        log.info("Product updated successfully: {}", code);

        return mapToResponse(updated);
    }

    @Override
    public void deleteProduct(String code) {
        log.info("Deleting product: {}", code);

        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + code));

        productRepository.delete(product);
        log.info("Product deleted successfully: {}", code);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProductAvailable(String productId, int quantity) {
        log.info("Checking availability for product {} with quantity {}", productId, quantity);

        Product product = productRepository.findByCode(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        return product.getAvailable() && product.getStockQuantity() >= quantity;
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory())
                .stockQuantity(product.getStockQuantity())
                .available(product.getAvailable())
                .build();
    }
}
