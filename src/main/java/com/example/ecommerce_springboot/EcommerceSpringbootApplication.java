package com.example.ecommerce_springboot;

import com.example.ecommerce_springboot.model.Product;
import com.example.ecommerce_springboot.model.User;
import com.example.ecommerce_springboot.repository.ProductRepository;
import com.example.ecommerce_springboot.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EcommerceSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceSpringbootApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(UserRepository repo, PasswordEncoder encoder) {
		return args -> {
			if (repo.findByUsername("admin") == null) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setEmail("admin@example.com"); // ✅ Add this line
				admin.setPassword(encoder.encode("admin123"));
				admin.setRole("ROLE_ADMIN");
				repo.save(admin);
			}
		};
	}
//	public CommandLineRunner run(ProductRepository productRepository) {
//		return args -> {
//			if (productRepository.count() == 0) {
//				Product p1 = new Product();
//				p1.setName("iPhone 15");
//				p1.setDescription("Apple smartphone");
//				p1.setPrice(999.99);
//				p1.setImageUrl("https://via.placeholder.com/150");
//
//				Product p2 = new Product();
//				p2.setName("Samsung Galaxy S24");
//				p2.setDescription("Samsung flagship phone");
//				p2.setPrice(899.99);
//				p2.setImageUrl("https://via.placeholder.com/150");
//
//				productRepository.save(p1);
//				productRepository.save(p2);
//			}
//		};
//	}
}
