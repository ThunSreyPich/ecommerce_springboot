package com.example.ecommerce_springboot.controller;

import com.example.ecommerce_springboot.model.Category;
import com.example.ecommerce_springboot.model.Product;
import com.example.ecommerce_springboot.repository.CategoryRepository;
import com.example.ecommerce_springboot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ShopController {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    // Constructor
    public ShopController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

//    @Autowired
//    private ProductRepository productRepository;

    @GetMapping("/shop")
    public String showShopPage(@RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               Model model) {

        List<Product> products;

        if (keyword != null && !keyword.isEmpty()) {
            if (category != null && !category.isEmpty()) {
                // filter by both category and keyword
                products = productRepository.findByNameContainingIgnoreCaseAndCategoryNameIgnoreCase(keyword, category);
            } else {
                // filter by keyword only
                products = productRepository.findByCategoryNameIgnoreCase(keyword);
            }
        } else if (category != null && !category.isEmpty()) {
            // filter by category only
            products = productRepository.findByCategoryNameIgnoreCase(category);
        } else {
            // no filters
            products = productRepository.findAll();
        }

        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);

        return "shop";
    }


}
