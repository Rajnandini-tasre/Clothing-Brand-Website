package com.example.online_shopping_springboot;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;


@Controller
public class PaymentController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/pay")
    public String processPayment(@RequestParam Long cartId, Model model) {
        List<CartItem> items = cartService.getCartItems(cartId);

        BigDecimal total = items.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Simulate payment success
        model.addAttribute("message", "Payment successful! Total paid: ₹" + total);

        // Optionally clear the cart
        cartService.clearCart(cartId);

        return "payment_success"; // This should be a valid HTML/Thymeleaf template
    }
}