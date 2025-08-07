package com.example.ecommerce_springboot.controller;

import com.example.ecommerce_springboot.model.Order;
import com.example.ecommerce_springboot.model.Product;
import com.example.ecommerce_springboot.model.User;
import com.example.ecommerce_springboot.repository.OrderRepository;
import com.example.ecommerce_springboot.repository.ProductRepository;
import com.example.ecommerce_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/order/buy/{productId}")
    public String buyProduct(@PathVariable Long productId, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));

        List<Product> products = new ArrayList<>();
        products.add(product);

        Order order = new Order(user, products, product.getPrice(), "COMPLETED");
        orderRepository.save(order);

        redirectAttributes.addFlashAttribute("success", "Purchase successful!");
        return "redirect:/shop";
    }

    @GetMapping("/order/history")
    public String viewOrderHistory(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        List<Order> orders = orderRepository.findByUser(user);
        model.addAttribute("orders", orders);
        return "order/history";
    }

    @GetMapping("/dashboard/orders")
    public String listOrders(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Order> orders = orderRepository.findAll();
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.trim().toLowerCase();
            orders = orders.stream()
                    .filter(order -> order.getProducts().stream()
                            .anyMatch(product -> product.getName().toLowerCase().contains(searchLower)))
                    .collect(Collectors.toList());
            model.addAttribute("searchTerm", search);
        }
        model.addAttribute("orders", orders);
        return "dashboard/orders";
    }

    @GetMapping("/dashboard/orders/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Order not found");
            return "redirect:/dashboard/orders";
        }
        model.addAttribute("order", order);
        return "dashboard/order-details";
    }
}