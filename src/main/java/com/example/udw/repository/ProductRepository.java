package com.example.udw.repository;

// - Repository cho bảng products
// - Cung cấp phương thức CRUD cơ bản
// - Quản lý sản phẩm

import com.example.udw.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}