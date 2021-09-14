package com.example.springbootloginauthentication.security;

import com.example.springbootloginauthentication.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    long ACCESS_TOKEN_DURATION = 1000 * 60 * 60 * 10; // 10 mins
    long REFREST_TOKEN_DURATION = 1000 * 60 * 60 * 24 * 7; // 1 week

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .deserializeJsonWith(new JacksonDeserializer<>())
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(User user) {
        return generateToken(user, ACCESS_TOKEN_DURATION);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, REFREST_TOKEN_DURATION);
    }

    private String generateToken(User user, long validDuration) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.getUsername(), validDuration);
    }

    private String createToken(Map<String, Object> claims, String subject, long validDuration) {
        return Jwts.builder()
                .serializeToJsonWith(new JacksonSerializer<>())
                .setClaims(claims) // any info to pass
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validDuration))
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
