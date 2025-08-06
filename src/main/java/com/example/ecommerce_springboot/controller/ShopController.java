package com.example.ecommerce_springboot.controller;

import com.example.ecommerce_springboot.model.Product;
import com.example.ecommerce_springboot.repository.ProductRepository;
import com.example.ecommerce_springboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class ShopController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("/shop")
    public String showShopPage(Model model) {
//        List<Product> products = productRepository.findAll();
//        model.addAttribute("products", products);
        model.addAttribute("products", productRepository.findAll());
//        model.addAttribute("pageTitle", "Product Shop");
        return "shop";
    }

    @GetMapping("/order/buy/{id}")
    @PreAuthorize("isAuthenticated()")
    public String buyProduct(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Product not found");
            return "redirect:/";
        }
        model.addAttribute("product", productOpt.get());
        return "buy-product";
    }

    @PostMapping("/order/confirm/{id}")
    @PreAuthorize("isAuthenticated()")
    public String confirmPurchase(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.createOrder(id);
            redirectAttributes.addFlashAttribute("success", "Purchase completed successfully!");
            return "redirect:/order/success";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Product not found");
            return "redirect:/";
        }
    }

    @GetMapping("/order/success")
    public String orderSuccess() {
        return "order-success";
    }

}
