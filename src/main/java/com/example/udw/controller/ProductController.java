// - Xử lý endpoint cho Product sử dụng Spring REST Controller
// - ADMIN: Tạo, sửa, xóa sản phẩm
// - CUSTOMER: Xem danh sách sản phẩm
// - Phân quyền: Kiểm tra session username và role, xử lý ngoại lệ khi dữ liệu không hợp lệ
package com.example.udw.controller;

import com.example.udw.entity.Product;
import com.example.udw.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    private boolean checkAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return session.getAttribute("username") != null && "ADMIN".equals(role);
    }

    private boolean checkLoggedIn(HttpSession session) {
        return session.getAttribute("username") != null;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createProduct(@RequestBody Map<String, Object> request, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        if (!checkAdmin(session)) {
            response.put("error", "Only ADMIN can create products");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String name = (String) request.get("name");
        String description = (String) request.get("description");
        Double price = request.get("price") != null ? Double.valueOf(request.get("price").toString()) : null;
        Integer stock = request.get("stock") != null ? Integer.valueOf(request.get("stock").toString()) : null;
        try {
            Product product = productService.createProduct(name, description, price, stock);
            response.put("message", "Product created: " + product.getId());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> request, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        if (!checkAdmin(session)) {
            response.put("error", "Only ADMIN can update products");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String name = (String) request.get("name");
        String description = (String) request.get("description");
        Double price = request.get("price") != null ? Double.valueOf(request.get("price").toString()) : null;
        Integer stock = request.get("stock") != null ? Integer.valueOf(request.get("stock").toString()) : null;
        try {
            Product product = productService.updateProduct(id, name, description, price, stock);
            response.put("message", "Product updated: " + product.getId());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        if (!checkAdmin(session)) {
            response.put("error", "Only ADMIN can delete products");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            productService.deleteProduct(id);
            response.put("message", "Product deleted: " + id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(HttpSession session) {
        if (!checkLoggedIn(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}