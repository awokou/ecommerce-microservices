package com.server.productservice.service.impl;

import com.server.productservice.domain.dto.request.ProductRequest;
import com.server.productservice.domain.dto.response.ProductResponse;
import com.server.productservice.domain.entity.Category;
import com.server.productservice.domain.entity.Product;
import com.server.productservice.exception.ResourceNotFoundException;
import com.server.productservice.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductRequest createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getProductCode());
        if (productRepository.existsByCode(request.getProductCode())) {
            throw new ResourceNotFoundException("Product code already exists: " + request.getProductCode());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category is not exists with given id : " + request.getCategoryId()));

        Product product = Product.builder()
                .code(request.getProductCode())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .category(category)
                .stockQuantity(request.getStockQuantity())
                .available(request.getStockQuantity() > 0)
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully: {}", savedProduct.getCode());

        return maProductRequest(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(String productCode) {
        log.info("Getting product: {}", productCode);
        Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        return mapToProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Getting all products");
        return productRepository.findAll()
                .stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        log.info("Getting products by category: {}", categoryId);
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProductsByName(String name) {
        log.info("Getting products by name: {}", name);
        return productRepository.findAllByName(name)
                .stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllAvailableTrue() {
        return productRepository.findByAvailableTrue()
                .stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductRequest updateProduct(Long id, ProductRequest request) {
        log.info("Updating product: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category is not exists with given id : " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
        product.setStockQuantity(request.getStockQuantity());
        product.setAvailable(request.getStockQuantity() > 0);

        Product updated = productRepository.save(product);
        log.info("Product updated successfully: {}", id);

        return maProductRequest(updated);
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        log.info("Product deleted successfully: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        productRepository.delete(product);
    }

    @Override
    @Transactional
    public boolean isProductAvailable(String productCode, int quantity) {
        log.info("Checking availability for product {} with quantity {}", productCode, quantity);
        Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productCode));

        return product.getAvailable() && product.getStockQuantity() >= quantity;
    }

    private ProductRequest maProductRequest(Product product) {
        if (product == null) {
            return null;
        }
        return ProductRequest.builder()
                .productCode(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory().getId())
                .stockQuantity(product.getStockQuantity())
                .build();
    }

    private ProductResponse mapToProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        return ProductResponse.builder()
                .id(product.getId())
                .productCode(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory().getName())
                .stockQuantity(product.getStockQuantity())
                .available(product.getAvailable())
                .build();
    }
}
