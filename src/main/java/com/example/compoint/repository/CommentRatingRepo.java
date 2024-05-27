package com.example.compoint.repository;

import com.example.compoint.entity.CommentRatingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommentRatingRepo extends CrudRepository<CommentRatingEntity, Long> {

    Optional<CommentRatingEntity> findByCommentIdAndUserId(Long commentId, Long userId);
}
