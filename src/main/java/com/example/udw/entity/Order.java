// - Định nghĩa entity Order cho bảng orders trong database sử dụng JPA, ánh xạ các trường id, user, product, quantity, totalPrice, orderDate
// - Lưu thông tin đơn hàng: id, user, product, quantity, totalPrice, orderDate
// - Dùng cho CUSTOMER (tạo, xem), xử lý ngoại lệ khi ánh xạ dữ liệu không hợp lệ
package com.example.udw.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;

    public Order() {}

    public Order(User user, Product product, Integer quantity, BigDecimal totalPrice, LocalDateTime orderDate) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
}