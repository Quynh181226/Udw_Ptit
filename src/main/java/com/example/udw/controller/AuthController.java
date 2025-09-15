//package com.example.udw.controller;
//
//// - Xử lý endpoint cho xác thực
//// - Đăng ký: Lưu user, log email xác nhận
//// - Đăng nhập: Trả access token và refresh token
//// - Refresh token: Gia hạn access token
//// - Validation ịt controller
//
//import com.example.udw.entity.RefreshToken;
//import com.example.udw.entity.User;
//import com.example.udw.security.JwtUtil;
//import com.example.udw.service.RefreshTokenService;
//import com.example.udw.service.UserService;
//import java.util.HashMap;
//import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//    private AuthenticationManager authenticationManager;
//    private UserService userService;
//    private RefreshTokenService refreshTokenService;
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Autowired
//    public void setUserService(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Autowired
//    public void setRefreshTokenService(RefreshTokenService refreshTokenService) {
//        this.refreshTokenService = refreshTokenService;
//    }
//
//    @Autowired
//    public void setJwtUtil(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    public AuthenticationManager getAuthenticationManager() { return authenticationManager; }
//    public UserService getUserService() { return userService; }
//    public RefreshTokenService getRefreshTokenService() { return refreshTokenService; }
//    public JwtUtil getJwtUtil() { return jwtUtil; }
//
//    @PostMapping("/register")
//    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
//        Map<String, String> response = new HashMap<>();
//        try {
//            User savedUser = userService.register(user);
//            response.put("message", "User registered: " + savedUser.getUsername());
//            return ResponseEntity.ok(response);
//        } catch (IllegalArgumentException e) {
//            response.put("error", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
//        Map<String, String> response = new HashMap<>();
//        String username = request.get("username");
//        String password = request.get("password");
//
//        if (username == null || username.isEmpty()) {
//            response.put("error", "Username is required");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//        if (password == null || password.isEmpty()) {
//            response.put("error", "Password is required");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password));
//        } catch (BadCredentialsException e) {
//            response.put("error", "Invalid credentials");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        }
//
//        UserDetails userDetails = userService.loadUserByUsername(username);
//        String accessToken = jwtUtil.generateToken(userDetails);
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userService.findByUsername(username));
//
//        response.put("accessToken", accessToken);
//        response.put("refreshToken", refreshToken.getToken());
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/refresh")
//    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
//        Map<String, String> response = new HashMap<>();
//        String refreshTokenStr = request.get("refreshToken");
//
//        // Validation trong controller
//        if (refreshTokenStr == null || refreshTokenStr.isEmpty()) {
//            response.put("error", "Refresh token is required");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//
//        try {
//            RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr);
//            refreshTokenService.verifyExpiration(refreshToken);
//            UserDetails userDetails = userService.loadUserByUsername(refreshToken.getUser().getUsername());
//            String accessToken = jwtUtil.generateToken(userDetails);
//            response.put("accessToken", accessToken);
//            return ResponseEntity.ok(response);
//        } catch (IllegalArgumentException e) {
//            response.put("error", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//    }
//}

// - Đăng ký: Lưu user, log email xác nhận, hỗ trợ role ADMIN/CUSTOMER
// - Đăng nhập: Kiểm tra và lưu username vào session
// - Phân quyền : Dùng session....

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
            response.put("message", "User registered: " + savedUser.getUsername());
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
            session.setAttribute("username", username); // Lưu username vào session
            session.setAttribute("role", user.getRole()); // Lưu role vào session
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}