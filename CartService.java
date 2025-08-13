package com.example.online_shopping_springboot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;
    
    public List<CartItem> getCartItems(Long cartId)
    {
        return cartItemRepository.findByCartId(cartId);
    }

    public CartItem addCartItem(CartItem cartItem) 
    {
        return cartItemRepository.save(cartItem);
    }

    @Transactional // ✅ Ensures delete query runs inside a transaction
    public void clearCart(Long cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
    }

    @Transactional // ✅ Needed for deleting the cart
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public void removeItemFromCart(Long cartId, Integer productId) 
    {
        CartItem item = cartItemRepository.findByCartIdAndProductId(cartId, productId);
        if (item != null) {
            cartItemRepository.delete(item);
        }
    }
     // ✅ New method to support addToCart via controller
    @Autowired
    private ProductRepository productRepository;

    public void addToCart(Long userId, Long productId) {
        // 1. Find or create cart for the user
        Cart cart = cartRepository.findByUserId(userId.intValue())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId.intValue());
                    return cartRepository.save(newCart);
                });

        // 2. Check if item already exists in the cart
        CartItem existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId.intValue());
        if (existingItem != null) {
            // Increase quantity
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            // ✅ Fetch product details
            Product product = productRepository.findById(productId.intValue())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));

            // ✅ Create new cart item with name and price
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getId());
            newItem.setProductId(product.getId());
            newItem.setProductName(product.getName());
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(1);
            cartItemRepository.save(newItem);
        }
    }
    
  
    public Cart getCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId).orElse(null);
    }
    
}