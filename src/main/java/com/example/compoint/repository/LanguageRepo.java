package com.example.compoint.repository;

import com.example.compoint.entity.LanguageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepo extends CrudRepository<LanguageEntity, Long> {
}
