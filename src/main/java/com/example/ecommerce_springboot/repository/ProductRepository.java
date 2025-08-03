package com.example.ecommerce_springboot.repository;


import com.example.ecommerce_springboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryNameIgnoreCase(String categoryName);
//    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByNameContainingIgnoreCaseAndCategoryNameIgnoreCase(String keyword, String category);

}
