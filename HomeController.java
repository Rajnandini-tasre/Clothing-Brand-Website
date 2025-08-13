package com.example.online_shopping_springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController 
{
    @GetMapping("/shopping")
    public String home() 
    {
        return "index";
    }
}