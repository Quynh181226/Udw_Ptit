// - Xử lý endpoint cho xác thực sử dụng Spring REST Controller
// - Đăng ký: Lưu user vào database với JPA, log email xác nhận, hỗ trợ role ADMIN/CUSTOMER, xử lý ngoại lệ khi dữ liệu không hợp lệ
// - Đăng nhập: Kiểm tra và lưu username vào session, xử lý ngoại lệ khi thông tin không hợp lệ
// - Phân quyền: Dùng session để lưu username và role

package com.example.udw.controller;

import com.example.udw.entity.User;
import com.example.udw.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            User savedUser = userService.register(user);

            String role = savedUser.getRole();
            if (role == null || role.isBlank()) {
                role = "CUSTOMER";
            }

            response.put("message", "User registered " + role + ": " + savedUser.getUsername());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        String username = request.get("username");
        String password = request.get("password");
        try {
            User user = userService.login(username, password);

            session.setAttribute("username", username);
            session.setAttribute("role", user.getRole());

            String role = user.getRole();
            if ("ADMIN".equalsIgnoreCase(role)) {
                response.put("message", "Login successful admin: " + username);
            } else if ("CUSTOMER".equalsIgnoreCase(role)) {
                response.put("message", "Login successful customer: " + username);
            } else {
                response.put("message", "Login successful: " + username);
            }
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}