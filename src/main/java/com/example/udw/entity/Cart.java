// - Định nghĩa entity Cart cho bảng carts trong database sử dụng JPA, ánh xạ các trường id, user, products với quan hệ 1-1 và Many-to-Many
// - Lưu giỏ hàng của user, liên kết 1-1 với User, chứa danh sách sản phẩm
// - Dùng cho CUSTOMER (thêm/xem), xử lý ngoại lệ khi ánh xạ dữ liệu không hợp lệ
package com.example.udw.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "cart_products",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    public Cart() {}

    public Cart(User user) {
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}