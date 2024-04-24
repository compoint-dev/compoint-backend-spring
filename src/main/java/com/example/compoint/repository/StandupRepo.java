package com.example.compoint.repository;

import com.example.compoint.entity.StandupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandupRepo extends CrudRepository <StandupEntity, Long> {
    StandupEntity findByName(String name);
    List<StandupEntity> findByUserId (Long id);
}
