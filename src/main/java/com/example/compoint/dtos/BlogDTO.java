package com.example.compoint.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogDTO {
    private Long id;
    private String content;
    private UserDTO user;
    private LocalDateTime createdAt;
}
