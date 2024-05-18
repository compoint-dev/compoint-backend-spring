package com.example.compoint.repository;

import com.example.compoint.entity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends CrudRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByToken(String token);
    void deleteByUsername(String username);
}