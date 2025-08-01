package com.example.ecommerce_springboot.controller;

import com.example.ecommerce_springboot.model.Product;
import com.example.ecommerce_springboot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShopController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/shop")
    public String showShopPage(Model model) {
        List<Product> products = productRepository.findAll();
        System.out.println("Found products: " + products);  // <- Debug output
        model.addAttribute("products", products);
        return "shop";
    }
}
