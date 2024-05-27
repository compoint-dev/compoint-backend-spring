package com.example.compoint.repository;

import com.example.compoint.entity.BlogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepo extends CrudRepository<BlogEntity, Long> {
    Optional<BlogEntity> findById(Integer id);
}
