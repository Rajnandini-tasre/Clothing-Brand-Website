package com.example.online_shopping_springboot;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
@Controller
public class ProductControllerUser {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/viewshoppinguser")
    public String viewProducts(Model model, HttpSession session) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        User user = (User) session.getAttribute("loggedInUser"); // Get user from session
        if (user != null) {
            model.addAttribute("user", user); // Add to model so Thymeleaf can use it
        }

        return "productlist_user";
    }
}