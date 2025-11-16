package com.javanauta.usuario.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Este método está 100% correto
    private Key getSigningKey() {
        String base64Key = this.secret;
        if (this.secret.contains("EXEMPLO:")) {
            base64Key = this.secret.split(":")[1].trim();
        }

        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            System.err.println("Chave JWT inválida como Base64. Usando string literal. Erro: " + e.getMessage());
            return Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
        }
    }

    // --- Geração (Sintaxe Moderna) ---
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                // ✅ SINTAXE MODERNA CORRETA
                .signWith(getSigningKey())
                .compact();
    }

    // --- Extração (Sintaxe Moderna) ---
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // ✅ SINTAXE MODERNA CORRETA (onde estava o erro)
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getSigningKey()).build();

        Jws<Claims> claimsJws = parser.parseClaimsJws(token);
        return claimsJws.getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}