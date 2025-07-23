package com.example.ecommerce_springboot.repository;


import com.example.ecommerce_springboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
