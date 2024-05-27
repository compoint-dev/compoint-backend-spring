package com.example.compoint.repository;

import com.example.compoint.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends CrudRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByName(String name);

    Optional<RoleEntity> findById(Integer id);
}
