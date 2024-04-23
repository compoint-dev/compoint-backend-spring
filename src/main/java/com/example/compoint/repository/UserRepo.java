package com.example.compoint.repository;

import com.example.compoint.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {
   Optional<UserEntity> findByUsername(String username);
   Optional<UserEntity> findById(Long id);
}