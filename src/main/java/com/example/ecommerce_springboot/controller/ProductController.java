//package com.example.ecommerce_springboot.controller;
//
//import com.example.ecommerce_springboot.model.Product;
//import com.example.ecommerce_springboot.model.Category;
//import com.example.ecommerce_springboot.repository.CategoryRepository;
//import com.example.ecommerce_springboot.repository.ProductRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.UUID;
//
//@Controller
//public class ProductController {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @GetMapping("/dashboard/products")
//    public String showProducts(Model model) {
//        model.addAttribute("products", productRepository.findAll());
//        model.addAttribute("categories", categoryRepository.findAll());
//        model.addAttribute("product", new Product());
//        return "dashboard/products"; // NOT "fragments/..." or ::content
//    }
//
//    @PostMapping("/dashboard/products/create")
//    public String createProduct(
//            @ModelAttribute Product product,
//            @RequestParam("imageFile") MultipartFile imageFile
//    ) {
//        if (!imageFile.isEmpty()) {
//            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
//            Path imagePath = Paths.get("src/main/resources/static/uploads/" + filename);
//
//            try {
//                Files.createDirectories(imagePath.getParent());
//                Files.write(imagePath, imageFile.getBytes());
//                product.setImage("/uploads/" + filename); // Save path to product
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        productRepository.save(product);
//        return "redirect:/dashboard/products";
//    }
//
//    //  Delete
//    @PostMapping("/dashboard/products/delete/{id}")
//    public String deleteProduct(@PathVariable Long id) {
//        productRepository.deleteById(id);
//        return "redirect:/dashboard/products";
//    }
//
//
//}
//
//

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
import java.util.Optional;
import java.util.UUID;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Show product list
    @GetMapping("/dashboard/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("product", new Product());
        return "dashboard/products";
    }


    // Create product
    @PostMapping("/dashboard/products/create")
    public String createProduct(
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile) {

        saveImageFile(product, imageFile);
        productRepository.save(product);
        return "redirect:/dashboard/products";
    }

    @GetMapping("/dashboard/products/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "dashboard/product-create";
    }

    // View product (returns modal or a page)
    @GetMapping("/dashboard/products/view/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "dashboard/product-view"; // create separate view template
    }

    // Show edit form
    @GetMapping("/dashboard/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return "redirect:/dashboard/products";
        }
        model.addAttribute("product", product.get());
        model.addAttribute("categories", categoryRepository.findAll());
        return "dashboard/product-edit"; // edit form template
    }

    // Update product
    @PostMapping("/dashboard/products/update/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute Product updatedProduct,
            @RequestParam("imageFile") MultipartFile imageFile) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setCategory(updatedProduct.getCategory());

        if (!imageFile.isEmpty()) {
            saveImageFile(product, imageFile);
        }

        productRepository.save(product);
        return "redirect:/dashboard/products";
    }


    // Delete
    @PostMapping("/dashboard/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/dashboard/products";
    }

    // Helper method to handle image upload
    private void saveImageFile(Product product, MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path imagePath = Paths.get("src/main/resources/static/uploads/" + filename);

            try {
                Files.createDirectories(imagePath.getParent());
                Files.write(imagePath, imageFile.getBytes());
                product.setImage("/uploads/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
