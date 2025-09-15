// - Xử lý logic nghiệp vụ cho Product
// - Tạo, sửa, xóa sản phẩm (cho ADMIN), lấy danh sách sản phẩm

package com.example.udw.service;

import com.example.udw.entity.Product;
import com.example.udw.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(String name, String description, Double price, Integer stock) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        Product product = new Product(name, description, BigDecimal.valueOf(price), stock);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, String name, String description, Double price, Integer stock) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID is required");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setName(name);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));
        product.setStock(stock);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID is required");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        productRepository.delete(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}