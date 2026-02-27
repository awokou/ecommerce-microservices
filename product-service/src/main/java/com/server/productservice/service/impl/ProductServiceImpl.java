package com.server.productservice.service.impl;

import com.server.productservice.domain.dto.request.ProductRequest;
import com.server.productservice.domain.dto.response.ProductResponse;
import com.server.productservice.domain.entity.Product;
import com.server.productservice.exception.ProductNotFoundException;
import com.server.productservice.repository.ProductRepository;
import com.server.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getProductCode());
        if (productRepository.existsByCode(request.getProductCode())) {
            throw new ProductNotFoundException("Product code already exists: " + request.getProductCode());
        }
        Product product = Product.builder()
                .code(request.getProductCode())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .category(request.getCategory())
                .stockQuantity(request.getStockQuantity())
                .available(request.getStockQuantity() > 0)
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully: {}", savedProduct.getCode());

        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(String productCode) {
        log.info("Getting product: {}", productCode);
        Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productCode));

        return mapToResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Getting all products");
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Getting products by category: {}", category);
        return productRepository.findByCategory(category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProductsByName(String name) {
        log.info("Getting products by name: {}", name);
        return productRepository.findAllByName(name)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllAvailableTrue() {
        return productRepository.findByAvailableTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(String productCode, ProductRequest request) {
        log.info("Updating product: {}", productCode);

        Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productCode));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(request.getCategory());
        product.setStockQuantity(request.getStockQuantity());
        product.setAvailable(request.getStockQuantity() > 0);

        Product updated = productRepository.save(product);
        log.info("Product updated successfully: {}", productCode);

        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(String productCode) {
        log.info("Deleting product: {}", productCode);
        Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productCode));

        productRepository.delete(product);
        log.info("Product deleted successfully: {}", productCode);
    }

    @Override
    @Transactional
    public boolean isProductAvailable(String productId, int quantity) {
        log.info("Checking availability for product {} with quantity {}", productId, quantity);
        Product product = productRepository.findByCode(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        return product.getAvailable() && product.getStockQuantity() >= quantity;
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productCode(product.getCode())
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
