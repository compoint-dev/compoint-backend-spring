package com.example.compoint.service;

import com.example.compoint.entity.RefreshTokenEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.repository.RefreshTokenRepo;
import com.example.compoint.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepo refreshTokenRepo;

    @Autowired
    UserRepo userRepo;

    @Value("${compoint.cookieExpiry}")
    private int cookieExpiry;
    public RefreshTokenEntity createOrUpdateRefreshToken(String username) {
        UserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }

        Optional<RefreshTokenEntity> existingToken = refreshTokenRepo.findByUserEntity(user);

        RefreshTokenEntity refreshToken;
        if (existingToken.isPresent()) {
            // If token exists, update it
            refreshToken = existingToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(cookieExpiry));
        } else {
            refreshToken = RefreshTokenEntity.builder()
                    .userEntity(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(cookieExpiry))
                    .build();
        }

        return refreshTokenRepo.save(refreshToken);
    }



    public Optional<RefreshTokenEntity> findByToken(String token){
        return refreshTokenRepo.findByToken(token);
    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepo.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

}