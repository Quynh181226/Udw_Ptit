package com.example.udw.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
//@Component
@Service
public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key";
    // 10 days
    private static final long EXPIRATION_TIME = 864_000_000;

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}