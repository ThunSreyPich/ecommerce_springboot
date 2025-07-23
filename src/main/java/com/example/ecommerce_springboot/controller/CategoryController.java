package com.example.ecommerce_springboot.controller;

import com.example.ecommerce_springboot.model.Category;
import com.example.ecommerce_springboot.repository.CategoryRepository;
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

    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findAllByOrderByIdDesc();
        model.addAttribute("categories", categories);
        return "dashboard/categories"; // Your view page
    }

    // Create category
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("isEdit", false);
        return "dashboard/category-form";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        System.out.println("💡 DEBUG: category.id = " + category.getId()); // check if ID is null or not

        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("success", "Category saved successfully!");
        return "redirect:/dashboard/categories";
    }

    //    Edit
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        model.addAttribute("isEdit", true);
        return "dashboard/category-form";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/dashboard/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/dashboard/categories";
    }
}
