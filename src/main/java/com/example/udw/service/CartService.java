// - Xử lý logic nghiệp vụ cho Cart sử dụng Spring Service
// - Thêm sản phẩm vào giỏ (cho CUSTOMER) với JPA, lấy giỏ của user, xử lý ngoại lệ khi username/productId không hợp lệ hoặc stock <= 0

package com.example.udw.service;

import com.example.udw.entity.Cart;
import com.example.udw.entity.Product;
import com.example.udw.entity.User;
import com.example.udw.repository.CartRepository;
import com.example.udw.repository.ProductRepository;
import com.example.udw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart addProductToCart(String username, Long productId) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.getRole().equals("CUSTOMER")) {
            throw new IllegalArgumentException("Only CUSTOMER can add to cart");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (product.getStock() <= 0) {
            throw new IllegalArgumentException("Product out of stock");
        }
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
        if (!cart.getProducts().contains(product)) {
            cart.getProducts().add(product);
            cartRepository.save(cart);
        }
        return cart;
    }

    public Cart getCartByUser(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.getRole().equals("CUSTOMER")) {
            throw new IllegalArgumentException("Only CUSTOMER can view cart");
        }
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }
}