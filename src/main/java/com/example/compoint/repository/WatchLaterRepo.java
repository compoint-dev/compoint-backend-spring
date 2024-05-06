package com.example.compoint.repository;

import com.example.compoint.entity.WatchLaterEntity;
import org.springframework.data.repository.CrudRepository;

public interface WatchLaterRepo extends CrudRepository<WatchLaterEntity, Long> {
    boolean existsByUserIdAndStandupId(Long userId, Long standupId);

    WatchLaterEntity findByUserIdAndStandupId(Long userId, Long standupId);
}
