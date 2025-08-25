package com.example.udw.entity;
//
//import lombok.Data;
//import jakarta.persistence.Id;
//import jakarta.persistence.Entity;
//
//@Entity
//@Data
//// ADMIN or CUSTOMER
//public class User {
//    @Id
//    private Long id;
//    private String username;
//    private String password;
//    private String email;
//    private String role;
//}
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    // hashed
    private String password;

    @Column(unique = true)
    private String email;

    // "ADMIN" or "CUSTOMER"
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}