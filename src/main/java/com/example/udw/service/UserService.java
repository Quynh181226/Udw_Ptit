// - Xử lý logic nghiệp vụ cho User
// - Đăng ký: Lưu user, log email xác nhận, hỗ trợ role ADMIN/CUSTOMER
// - Đăng nhập: Kiểm tra username/password, trả về user

package com.example.udw.service;

import com.example.udw.entity.User;
import com.example.udw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (user.getRole() == null || (!user.getRole().equals("ADMIN") && !user.getRole().equals("CUSTOMER"))) {
            throw new IllegalArgumentException("Role must be ADMIN or CUSTOMER");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User savedUser = userRepository.save(user);
        System.out.println("Registration confirmation for " + savedUser.getEmail() + ": Welcome, " + savedUser.getUsername() + "!");
        return savedUser;
    }

    public User login(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }
}