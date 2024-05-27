package com.example.compoint.dtos;

import java.time.LocalDateTime;

public class BlogDTO {
    private Long id;
    private String content;
    private UserDTO user;
    private LocalDateTime createdAt;
}
