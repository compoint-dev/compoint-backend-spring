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
    private String standupName;
    private String comment;
    private Long rating;
    private LocalDateTime createdAt;

}
