package com.example.ecommerce_springboot.controller;

import com.example.ecommerce_springboot.model.Category;
import com.example.ecommerce_springboot.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dashboard/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ===========================
    // HTML Web Pages (Thymeleaf)
    // ===========================

    // List categories page
    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findAllByOrderByIdDesc();
        model.addAttribute("categories", categories);
        return "dashboard/categories";
    }

    // Show form to create new category (HTML)
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("isEdit", false);
        return "dashboard/category-form";
    }

    // Save new category (from form POST)
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("success", "Category saved successfully!");
        return "redirect:/dashboard/categories";
    }

    // Show form to edit category (HTML)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            redirectAttributes.addFlashAttribute("error", "Category not found.");
            return "redirect:/dashboard/categories";
        }
        model.addAttribute("category", category);
        model.addAttribute("isEdit", true);
        return "dashboard/category-form";
    }

    // Update category (from form POST)
    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("success", "Category updated successfully!");
        return "redirect:/dashboard/categories";
    }

    // Delete category (from form POST)
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Category not found.");
        }
        return "redirect:/dashboard/categories";
    }

    // ===========================
    // JSON API endpoints for Postman / AJAX
    // ===========================

    // Get all categories as JSON
    @GetMapping("/api/list")
    @ResponseBody
    public List<Category> getAllAsJson() {
        return categoryRepository.findAllByOrderByIdDesc();
    }

    // Get category by ID as JSON
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new category via JSON
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        if (category.getId() != null) {
            return ResponseEntity.badRequest().build(); // ID must be null for new
        }
        Category saved = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    // Update category via JSON
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category catDetails) {
        return categoryRepository.findById(id)
                .map(cat -> {
                    cat.setName(catDetails.getName());
                    // update other fields if needed
                    Category updated = categoryRepository.save(cat);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete category via JSON
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteCategoryApi(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
