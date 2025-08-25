package com.example.udw.controller;

import com.example.udw.config.JwtUtil;
import com.example.udw.service.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class Auth {
    @Autowired
    private User userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private com.example.udw.repository.User userRepository;

    @PostMapping("/register")
    public String register(@RequestBody com.example.udw.entity.User user) {
        userRepository.save(user);
        return "User registered: " + user.getUsername();
    }
    @GetMapping
    public String getAuthInfo() {
        return "Auth endpoint";
    }
    @PostMapping("/login")
    public String login(@RequestBody com.example.udw.entity.User user) {
        return "Login success for: " + user.getUsername();
    }
}