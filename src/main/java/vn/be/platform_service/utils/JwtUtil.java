package vn.be.platform_service.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.access-token-exp}") long accessTokenExpiration, @Value("${jwt.refresh-token-exp}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // ===== Tạo Access Token =====
    public String generateAccessToken(String username) {
        return Jwts.builder().subject(username).claim("type", "access").issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + accessTokenExpiration)).signWith(secretKey).compact();
    }

    // ===== Tạo Refresh Token =====
    public String generateRefreshToken(String username) {
        return Jwts.builder().subject(username).claim("type", "refresh").issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration)).signWith(secretKey).compact();
    }

    // ===== Lấy username từ token =====
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // ===== Lấy loại token (access/refresh) =====
    public String extractTokenType(String token) {
        return parseClaims(token).get("type", String.class);
    }

    // ===== Kiểm tra token có hợp lệ không =====
    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // ===== Parse claims từ token =====
    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
