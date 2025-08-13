package com.example.online_shopping_springboot;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> 
{
	User findByUsernameAndPassword(String username, String password);
}