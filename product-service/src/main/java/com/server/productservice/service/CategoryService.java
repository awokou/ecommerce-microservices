package com.server.productservice.service;

import java.util.List;

import com.server.productservice.domain.dto.request.CategoryRequest;
import com.server.productservice.domain.dto.response.CategoryResponse;

public interface CategoryService {

    CategoryRequest createCategory(CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategory();

    CategoryRequest updateCategory(Long id, CategoryRequest request);

    void deleteCategoryById(Long id);

}
