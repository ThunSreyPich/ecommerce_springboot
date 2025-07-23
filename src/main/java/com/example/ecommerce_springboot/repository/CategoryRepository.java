package com.example.ecommerce_springboot.repository;

import com.example.ecommerce_springboot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}