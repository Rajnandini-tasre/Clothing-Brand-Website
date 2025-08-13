package com.example.online_shopping_springboot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index() {
        return "index"; // returns index.html
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // to bind the form
        return "register"; // returns register.html
        
        
    }
    
    @PostMapping("/register1")
    public String registerUser(@ModelAttribute("user") User user) {
        userRepository.save(user);
        return "adminhome"; // or redirect to a success page
    }
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User()); // to bind the form
        return "login"; // returns register.html
        
        
    }
    
    @PostMapping("/login1")
    public String loginUser(@ModelAttribute("user") User loginUser, Model model, HttpSession session) {
        User existingUser = userRepository.findByUsernameAndPassword(
            loginUser.getUsername(), loginUser.getPassword()
        );

        if (existingUser != null && existingUser.isEnabled()) {
            session.setAttribute("loggedInUser", existingUser); // Store user in session
            model.addAttribute("user", existingUser);

            if ("ADMIN".equalsIgnoreCase(existingUser.getRole())) {
                return "adminhome";
            } else {
                return "userhome";
            }
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/userhome")
    public String userHome(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            // user not logged in, redirect to login page
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser);
        return "userhome";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clear the session
        return "redirect:/login"; // Redirect to login page
    }
   
}