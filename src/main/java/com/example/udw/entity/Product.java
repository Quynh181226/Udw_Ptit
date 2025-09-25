// - Định nghĩa entity Product cho bảng products trong database sử dụng JPA, ánh xạ các trường id, name, description, price, stock
// - Lưu thông tin sản phẩm: id, name, description, price, stock
// - Dùng cho ADMIN (tạo/sửa/xóa), CUSTOMER (xem), xử lý ngoại lệ khi ánh xạ dữ liệu không hợp lệ

package com.example.udw.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    public Product() {}

    public Product(String name, String description, BigDecimal price, Integer stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}