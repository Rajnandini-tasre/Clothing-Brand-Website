package com.example.online_shopping_springboot;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    // Find a cart by userId
    
    Optional<Cart> findByUserId(Integer userId);
    // Optionally: check if cart exists
    boolean existsByUserId(Integer userId);
}