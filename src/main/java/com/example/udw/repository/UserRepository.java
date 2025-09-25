// - Repository cho bảng users sử dụng Spring Data JPA
// - Cung cấp phương thức tìm user bằng username và email

package com.example.udw.repository;

import com.example.udw.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}