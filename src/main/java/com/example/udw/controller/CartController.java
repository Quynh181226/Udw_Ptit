// - Xử lý endpoint cho Cart sử dụng Spring REST Controller
// - CUSTOMER: Thêm sản phẩm vào giỏ, xem giỏ
// - Phân quyền: Kiểm tra session username và role CUSTOMER, xử lý ngoại lệ IllegalArgumentException khi dữ liệu không hợp lệ

package com.example.udw.controller;

import com.example.udw.entity.Cart;
import com.example.udw.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    private boolean checkCustomer(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return session.getAttribute("username") != null && "CUSTOMER".equals(role);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addProductToCart(
            @RequestBody Map<String, Long> request, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        if (!checkCustomer(session)) {
            response.put("error", "Only CUSTOMER can add to cart");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        Long productId = request.get("productId");
        String username = (String) session.getAttribute("username");
        try {
            Cart cart = cartService.addProductToCart(username, productId);
            response.put("message", "Product added to cart: " + productId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(HttpSession session) {
        if (!checkCustomer(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String username = (String) session.getAttribute("username");
        try {
            Cart cart = cartService.getCartByUser(username);
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}