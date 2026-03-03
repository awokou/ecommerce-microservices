package com.server.productservice.service;

import com.server.productservice.domain.dto.request.ProductRequest;
import com.server.productservice.domain.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductRequest createProduct(ProductRequest request);

    ProductResponse getProduct(String productCode);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getProductsByCategoryId(Long categoryId);

    List<ProductResponse> getAllProductsByName(String productName);

    List<ProductResponse> getAllAvailableTrue();

    ProductRequest updateProduct(Long id, ProductRequest request);

    void deleteProductById(Long id);

    boolean isProductAvailable(String productCode, int quantity);
}
