package com.example.compoint.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String username;
    private String standupName;
    private String comment;
    private Long rating;
    private LocalDateTime createdAt;

}
