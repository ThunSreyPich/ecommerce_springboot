package com.example.ecommerce_springboot.repository;

import com.example.ecommerce_springboot.model.Order;
import com.example.ecommerce_springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}