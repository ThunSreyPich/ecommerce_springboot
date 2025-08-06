//package com.example.ecommerce_springboot.config;
//
//import com.example.ecommerce_springboot.service.UserDetailsServiceImpl;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.*;
//import org.springframework.security.config.annotation.web.builders.*;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.*;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final UserDetailsServiceImpl userDetailsService;
//
//    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable() // Disable CSRF for development & Postman use
//
////                .authorizeHttpRequests(auth -> auth
////                        // Public pages
////                        .requestMatchers("/", "/shop/**", "/login", "/css/**", "/js/**").permitAll()
////
////                        // Public API access (e.g., for Postman or mobile apps)
////                        .requestMatchers("/api/**").permitAll()
////
////                        // Admin dashboard
////                        .requestMatchers("/dashboard/**").hasRole("ADMIN")
////
////                        // Everything else
////                        .anyRequest().authenticated()
////                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/shop/**", "/login", "/css/**", "/js/**").permitAll()
//                        .requestMatchers("/api/**", "/dashboard/categories/api/**").permitAll()
//                        .requestMatchers("/dashboard/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
//
//                // Form login config
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/dashboard", true)
//                        .permitAll()
//                )
//
//                // Logout config
//                .logout(logout -> logout.permitAll());
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}

package com.example.ecommerce_springboot.config;

import com.example.ecommerce_springboot.model.User;
import com.example.ecommerce_springboot.repository.UserRepository;
import com.example.ecommerce_springboot.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService; // ✅ Inject this

    public SecurityConfig(UserRepository userRepository, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/shop", "/css/**", "/js/**", "/uploads/**").permitAll()
                        .requestMatchers("/dashboard/**").hasRole("ADMIN")
                        .requestMatchers("/order/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customSuccessHandler())
                        .failureUrl("/login?error=true") // ✅ Add this line
                        .permitAll()
                )
                .userDetailsService(userDetailsService) // ✅ Tell Spring to use your DB auth
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/shop")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            User user = userRepository.findByUsername(username);
//            if (user == null) {
//                throw new UsernameNotFoundException("User not found");
//            }
//            return org.springframework.security.core.userdetails.User
//                    .withUsername(user.getUsername())
//                    .password(user.getPassword())
//                    .roles(user.getRole().replace("ROLE_", ""))
//                    .build();
//        };
//    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/dashboard/products");
            } else {
                response.sendRedirect("/shop");
            }
        };
    }
}
