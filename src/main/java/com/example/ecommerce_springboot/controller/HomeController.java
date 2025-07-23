package com.example.ecommerce_springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/shop")
    public String shop() {
        return "shop";
    }
    @GetMapping("/dashboard/products")
    public String productsPage() {
        return "dashboard/products";
    }

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
}
