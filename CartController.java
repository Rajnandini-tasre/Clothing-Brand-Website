package com.example.online_shopping_springboot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.Collections;
import java.util.List;


import java.util.List;

@Controller
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/addToCart/{productId}")
    public String addToCartViaGet(@PathVariable Long productId, @RequestParam Long userId) {
        cartService.addToCart(userId, productId); // ✅ This actually adds the product to cart

        return "redirect:/viewshoppinguser"; // Redirect back to product list or user home
    }
    
    
    @GetMapping("/{cartId}/items")
    public List<CartItem> getCartItems(@PathVariable Long cartId) {
        return cartService.getCartItems(cartId);
    }

    @PostMapping("/{cartId}/add")
    public CartItem addItemToCart(@PathVariable Long cartId, @RequestBody CartItem cartItem) {
        cartItem.setCartId(cartId);
        return cartService.addCartItem(cartItem);
    }

    @DeleteMapping("/{cartId}/clear")
    public void clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
    }

    @DeleteMapping("/{cartId}/item/{productId}")
    public void removeItemFromCart(@PathVariable Long cartId, @PathVariable Integer productId) {
        cartService.removeItemFromCart(cartId, productId);
    }
    
    @GetMapping("/mycart")
    public String showCart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        // Get the user's cart
        Cart cart = cartService.getCartByUserId(user.getId());
        if (cart != null) {
            model.addAttribute("cartItems", cartService.getCartItems(cart.getId()));
        } else {
            model.addAttribute("cartItems", Collections.emptyList());
        }

        return "cart"; // ✅ renders cart.html
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getCartByUserId(user.getId());

        if (cart != null) {
            cartService.clearCart(cart.getId());      // delete from cart_items
            cartService.deleteCart(cart.getId());     // delete from cart table
        }

        model.addAttribute("username", user.getUsername());
        return "order_confirmation"; // will render order_confirmation.html
    }
}