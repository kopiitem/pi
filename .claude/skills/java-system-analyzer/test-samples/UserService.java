package com.example.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.model.User;
import java.util.List;
import java.util.Optional;

/**
 * User Service
 * Handles business logic for user management
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * Find all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    /**
     * Find user by ID
     */
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
    
    /**
     * Find user by email
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Save new user
     */
    public User save(User user) {
        // Validate user data
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // Check if user already exists
        User existingUser = findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("User already exists");
        }
        
        // Save user
        User savedUser = userRepository.save(user);
        
        // Send welcome email
        emailService.sendWelcomeEmail(savedUser);
        
        return savedUser;
    }
    
    /**
     * Update existing user
     */
    public User update(Long id, User userDetails) {
        User user = findById(id);
        if (user == null) {
            return null;
        }
        
        if (userDetails.getName() != null) {
            user.setName(userDetails.getName());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Delete user
     */
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    
    /**
     * Authenticate user
     */
    public boolean authenticate(String email, String password) {
        User user = findByEmail(email);
        if (user != null) {
            return user.getPassword().equals(password); // In production, use bcrypt
        }
        return false;
    }
}
