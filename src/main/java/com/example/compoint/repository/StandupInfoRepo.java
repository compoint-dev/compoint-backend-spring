package com.example.compoint.repository;

import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.StandupInfoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StandupInfoRepo extends CrudRepository<StandupInfoEntity, Long> {
//    List<StandupInfoEntity> findTop5ByCreatedAtAfterOrderByRatingDesc(LocalDateTime dateTime);
}
