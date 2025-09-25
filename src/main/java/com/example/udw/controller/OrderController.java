// - Xử lý endpoint cho Order sử dụng Spring REST Controller
// - CUSTOMER: Tạo đơn hàng, xem lịch sử đơn hàng
// - Phân quyền: Kiểm tra session username và role CUSTOMER, xử lý ngoại lệ khi dữ liệu không hợp lệ

package com.example.udw.controller;

import com.example.udw.entity.Order;
import com.example.udw.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private boolean checkCustomer(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return session.getAttribute("username") != null && "CUSTOMER".equals(role);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(
            @RequestBody Map<String, Object> request, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        if (!checkCustomer(session)) {
            response.put("error", "Only CUSTOMER can create order");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        Long productId = request.get("productId") != null ? Long.valueOf(request.get("productId").toString()) : null;
        Integer quantity = request.get("quantity") != null ? Integer.valueOf(request.get("quantity").toString()) : null;
        String username = (String) session.getAttribute("username");
        try {
            Order order = orderService.createOrder(username, productId, quantity);
            response.put("message", "Order created: " + order.getId());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(HttpSession session) {
        if (!checkCustomer(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String username = (String) session.getAttribute("username");
        try {
            List<Order> orders = orderService.getOrdersByUser(username);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}