package com.example.ecommerce_springboot.controller;

import com.example.ecommerce_springboot.model.Product;
import com.example.ecommerce_springboot.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

//    @GetMapping("/shop")
//    public String shop() {
//        return "shop";
//    }

//    @GetMapping("/dashboard/categories")
//    public String categoriesPage() {
//        return "dashboard/categories";
//    }

    @GetMapping("/dashboard/users")
    public String usersPage() {
        return "dashboard/users";
    }

    @GetMapping("/dashboard/orders")
    public String ordersPage() {
        return "dashboard/orders";
    }

//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @GetMapping("/dashboard/products")
//    public String productsPage(Model model) {
//        model.addAttribute("product", new Product());
//        model.addAttribute("categories", categoryRepository.findAll());
//        return "dashboard/products";  // your Thymeleaf template path
//    }
}

