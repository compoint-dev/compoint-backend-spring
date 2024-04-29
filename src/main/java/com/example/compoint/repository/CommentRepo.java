package com.example.compoint.repository;

import com.example.compoint.dtos.CommentDTO;
import com.example.compoint.entity.CommentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepo extends CrudRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByStandupId(Long standupId);
    List<CommentEntity> findAllByUser_Id(Long userId);  // Используйте _ для доступа к свойствам связанных сущностей

}
