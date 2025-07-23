package com.example.ecommerce_springboot.controller;

import com.example.ecommerce_springboot.model.Product;
import com.example.ecommerce_springboot.model.Category;
import com.example.ecommerce_springboot.repository.CategoryRepository;
import com.example.ecommerce_springboot.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/dashboard/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("product", new Product());
        return "dashboard/products"; // NOT "fragments/..." or ::content
    }

    @PostMapping("/dashboard/products/create")
    public String createProduct(
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile
    ) {
        if (!imageFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path imagePath = Paths.get("src/main/resources/static/uploads/" + filename);

            try {
                Files.createDirectories(imagePath.getParent());
                Files.write(imagePath, imageFile.getBytes());
                product.setImage("/uploads/" + filename); // Save path to product
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productRepository.save(product);
        return "redirect:/dashboard/products";
    }

    //  Delete
    @PostMapping("/dashboard/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/dashboard/products";
    }


}


