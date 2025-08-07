package com.example.ecommerce_springboot.controller;

import com.example.ecommerce_springboot.model.User;
import com.example.ecommerce_springboot.repository.OrderRepository;
import com.example.ecommerce_springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(User user, RedirectAttributes redirectAttributes) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/register";
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/dashboard/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "dashboard/users";
    }

    @GetMapping("/dashboard/users/{id}")
    public String viewUser(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/dashboard/users";
        }
        model.addAttribute("user", user);
        return "dashboard/user-details";
    }

    @PostMapping("/dashboard/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/dashboard/users";
        }
        // Delete associated orders to avoid foreign key constraint violation
        orderRepository.deleteAll(orderRepository.findByUser(user));
        userRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "User and associated orders deleted successfully");
        return "redirect:/dashboard/users";
    }
}