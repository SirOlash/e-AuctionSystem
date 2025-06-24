package com.OlashAuctionSystem.util;

import com.OlashAuctionSystem.data.models.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "OlashAuctionSystemOlashAuctionSystem";
    private final Key signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

//    private static final long EXPIRATION_TIME = 420000;

    public String generateToken(Map<String, Object> extraClaims, String subject, Role role) {
        Date now = new Date();
        long expirationTime;
        if (role == Role.ADMIN){
            expirationTime = 1800000;
        }
        else {
            expirationTime = 1200000;
        }
//        long expirationTime = switch(role){
//            case ADMIN -> 1800000;
//            default -> 1200000;
//        };
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String subject) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject().equals(subject)
                && !claims.getExpiration().before(new Date());
    }


}
