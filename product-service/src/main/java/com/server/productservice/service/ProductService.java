package com.server.productservice.service;

import com.server.productservice.dto.request.ProductRequest;
import com.server.productservice.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProduct(String code);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getProductsByCategory(String category);

    ProductResponse updateProduct(String code, ProductRequest request);

    void deleteProduct(String code);

    boolean isProductAvailable(String code, int quantity);
}
