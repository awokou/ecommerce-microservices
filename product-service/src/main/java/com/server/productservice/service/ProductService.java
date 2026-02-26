package com.server.productservice.service;

import com.server.productservice.domain.dto.request.ProductRequest;
import com.server.productservice.domain.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProduct(String code);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getProductsByCategory(String category);

    List<ProductResponse> getAllProductsByName(String name);

    List<ProductResponse> getAllAvailableTrue();

    ProductResponse updateProduct(String code, ProductRequest request);

    void deleteProduct(String code);

    boolean isProductAvailable(String code, int quantity);
}
