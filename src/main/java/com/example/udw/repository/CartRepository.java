package com.example.udw.repository;

// - Repository cho bảng carts sử dụng Spring Data JPA
// - Tìm giỏ hàng theo user

import com.example.udw.entity.Cart;
import com.example.udw.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}