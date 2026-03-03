package com.server.productservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.server.productservice.domain.dto.request.CategoryRequest;
import com.server.productservice.domain.dto.response.CategoryResponse;
import com.server.productservice.domain.entity.Category;
import com.server.productservice.exception.ResourceNotFoundException;
import com.server.productservice.repository.CategoryRepository;
import com.server.productservice.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryRequest createCategory(CategoryRequest request) {
        log.info("Creating category: {}", request.getName());
        if (categoryRepository.existsByName(request.getName())) {
            throw new ResourceNotFoundException("Category name already exists: " + request.getName());
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        Category saveCategory = categoryRepository.save(category);

        return mapToCategoryRequest(saveCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        log.info("Getting category: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));

        return mapToCategoryResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategory() {
        log.info("Getting all category");
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToCategoryResponse)
                .toList();
    }

    @Override
    @Transactional
    public CategoryRequest updateCategory(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category is not exists with given id : " + id));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return mapToCategoryRequest(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        log.info("Category deleted successfully: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));

        categoryRepository.delete(category);
    }

    private CategoryRequest mapToCategoryRequest(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryRequest.builder()
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
