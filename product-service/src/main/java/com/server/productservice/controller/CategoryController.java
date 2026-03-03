package com.server.productservice.controller;

import com.server.productservice.domain.dto.request.CategoryRequest;
import com.server.productservice.domain.dto.response.CategoryResponse;
import com.server.productservice.service.CategoryService;
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
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category")
    @PostMapping
    public ResponseEntity<CategoryRequest> createProduct(@Valid @RequestBody CategoryRequest request) {
        log.info("Rest request to create category: {}", request.getName());
        CategoryRequest category = categoryService.createCategory(request);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        log.info("Rest request to get category: {}", id);
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Get all categorys")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategory() {
        log.info("Rest request to get all categorys");
        List<CategoryResponse> categorys = categoryService.getAllCategory();
        return ResponseEntity.ok(categorys);
    }

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryRequest> updateCategory(@PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        log.info("Rest request to update category: {}", id);
        CategoryRequest category = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Delete category")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        log.info("Rest request to delete category: {}", id);
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
