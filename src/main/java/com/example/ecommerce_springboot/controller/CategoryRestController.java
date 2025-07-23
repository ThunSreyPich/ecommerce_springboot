package com.example.ecommerce_springboot.controller;


import com.example.ecommerce_springboot.model.Category;
import com.example.ecommerce_springboot.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {
    private final CategoryRepository categoryRepository;

    public CategoryRestController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Get all
    @GetMapping
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        if (category.getId() != null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID should not be provided when creating"));
        }
        Category saved = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Category created successfully!",
                        "category", saved
                ));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryRepository.findById(id)
                .map(existing -> {
                    existing.setName(category.getName());
                    Category updated = categoryRepository.save(existing);

                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Category updated successfully!");
                    response.put("category", updated);

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Category not found")));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Category not found"));
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Category deleted successfully!"));
    }

}
