package com.example.demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("bankingsecretkey12345678901234567890".getBytes());
    private static final long EXPIRY_TIME = 3600000; // 1 hour

    // generate token
    public static String generateToken(String accno) {
        return Jwts.builder()
                .setSubject(accno)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // verify token and get accno from it
    public static String validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}