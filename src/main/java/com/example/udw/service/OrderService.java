// - Xử lý logic nghiệp vụ cho Order sử dụng Spring Service
// - Tạo đơn hàng (cho CUSTOMER) với JPA, xem lịch sử đơn hàng, xử lý ngoại lệ khi username/productId/quantity không hợp lệ hoặc stock không đủ
package com.example.udw.service;

import com.example.udw.entity.Order;
import com.example.udw.entity.Product;
import com.example.udw.entity.User;
import com.example.udw.repository.OrderRepository;
import com.example.udw.repository.ProductRepository;
import com.example.udw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(String username, Long productId, Integer quantity) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required");
        }

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getRole().equals("CUSTOMER")) {
            throw new IllegalArgumentException("Only CUSTOMER can create order");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock");
        }

        if(product==null) {
            throw new IllegalArgumentException("Product empty");
        }

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        Order order = new Order(user, product, quantity, totalPrice, LocalDateTime.now());
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getRole().equals("CUSTOMER")) {
            throw new IllegalArgumentException("Only CUSTOMER can view orders");
        }

        return orderRepository.findByUser(user);
    }
}