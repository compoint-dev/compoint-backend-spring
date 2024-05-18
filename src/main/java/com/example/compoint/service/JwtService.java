package com.example.compoint.service;

import com.example.compoint.entity.RefreshTokenEntity;
import com.example.compoint.repository.RefreshTokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RefreshTokenRepo refreshTokenRepo;

    @Value("${compoint.jwtSecret}")
    private String SECRET;

    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60; // 1 hour
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7; // 7 days

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
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .setAllowedClockSkewSeconds(350)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && validateToken(token));
    }

    public Boolean validateRefreshToken(String token) {
        Optional<RefreshTokenEntity> refreshTokenOpt = refreshTokenRepo.findByToken(token);
        if (refreshTokenOpt.isPresent()) {
            RefreshTokenEntity refreshToken = refreshTokenOpt.get();
            return !refreshToken.getExpirationDate().before(new Date());
        }
        return false;
    }

    public String generateAccessToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, username, ACCESS_TOKEN_VALIDITY);
    }

    @Transactional
    public String generateRefreshToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        String token = createToken(claims, username, REFRESH_TOKEN_VALIDITY);

        refreshTokenRepo.deleteByUsername(username);

        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setToken(token);
        refreshToken.setUsername(username);
        refreshToken.setExpirationDate(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY));
        refreshTokenRepo.save(refreshToken);

        return token;
    }

    private String createToken(Map<String, Object> claims, String username, Long validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
