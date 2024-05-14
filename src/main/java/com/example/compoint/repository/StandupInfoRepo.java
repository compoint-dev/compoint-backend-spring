package com.example.compoint.repository;

import com.example.compoint.entity.StandupInfoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StandupInfoRepo extends CrudRepository<StandupInfoEntity, Long> {
    Optional<StandupInfoEntity> findById(Long id);
    List<StandupInfoEntity> findTop5ByCreatedAtAfterOrderByRatingDesc(LocalDateTime dateTime);
}
