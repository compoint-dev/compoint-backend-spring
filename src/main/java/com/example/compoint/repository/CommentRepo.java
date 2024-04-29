package com.example.compoint.repository;

import com.example.compoint.entity.CommentEntity;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepo extends CrudRepository<CommentEntity, Long> {
}
