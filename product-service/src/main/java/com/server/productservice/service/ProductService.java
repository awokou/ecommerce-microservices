package com.server.productservice.service;

import com.server.productservice.domain.dto.request.ProductRequest;
import com.server.productservice.domain.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProduct(String productCode);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getProductsByCategory(String category);

    List<ProductResponse> getAllProductsByName(String productName);

    List<ProductResponse> getAllAvailableTrue();

    ProductResponse updateProduct(String productCode, ProductRequest request);

    void deleteProduct(String productCode);

    boolean isProductAvailable(String productCode, int quantity);
}
