package com.example.compoint.repository;

import com.example.compoint.entity.RefreshTokenEntity;
import com.example.compoint.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends CrudRepository<RefreshTokenEntity, Integer> {
    Optional<RefreshTokenEntity> findByToken(String token);
    Optional<RefreshTokenEntity> findByUserEntity(UserEntity user);

}