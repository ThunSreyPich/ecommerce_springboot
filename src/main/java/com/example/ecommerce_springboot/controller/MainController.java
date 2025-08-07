package com.example.ecommerce_springboot.controller;

import com.example.ecommerce_springboot.repository.CategoryRepository;
import com.example.ecommerce_springboot.repository.OrderRepository;
import com.example.ecommerce_springboot.repository.ProductRepository;
import com.example.ecommerce_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String shop() {
        return "shop"; // shop.html
    }

//    @GetMapping("/dashboard")
//    public String dashboard() {
//        return "dashboard"; // dashboard.html
//    }

//    @GetMapping("/login")
//    public String login() {
//        return "login"; // login.html
//    }

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("productCount", productRepository.count());
        model.addAttribute("categoryCount", categoryRepository.count());
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("orderCount", orderRepository.count());
        return "dashboard";
    }
}