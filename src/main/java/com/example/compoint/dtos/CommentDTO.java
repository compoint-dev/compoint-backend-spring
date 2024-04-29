package com.example.compoint.dtos;

import com.example.compoint.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String username;
    private String comment;
    private LocalDateTime createdAt;
    private Long rating;

    public CommentDTO(CommentEntity commentEntity) {
        this.id = commentEntity.getId();
        this.username = commentEntity.getUser().getUsername();
        this.comment = commentEntity.getComment();
        this.createdAt = commentEntity.getCreatedAt();
        this.rating = commentEntity.getRating();
    }
}
