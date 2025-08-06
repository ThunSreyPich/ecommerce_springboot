package com.example.ecommerce_springboot.service;

import com.example.ecommerce_springboot.model.Order;
import com.example.ecommerce_springboot.model.Product;
import com.example.ecommerce_springboot.model.User;
import com.example.ecommerce_springboot.repository.OrderRepository;
import com.example.ecommerce_springboot.repository.ProductRepository;
import com.example.ecommerce_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByUsername(username));
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Product product = productOpt.get();
        Order order = new Order(
                userOpt.get(),
                Collections.singletonList(product),
                product.getPrice(),
                "COMPLETED"
        );
        return orderRepository.save(order);
    }
}