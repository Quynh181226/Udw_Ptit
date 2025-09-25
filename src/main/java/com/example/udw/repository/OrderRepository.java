package com.example.udw.repository;

// - Repository cho bảng orders sử dụng Spring Data JPA
// - Tìm danh sách đơn hàng theo user
import com.example.udw.entity.Order;
import com.example.udw.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}