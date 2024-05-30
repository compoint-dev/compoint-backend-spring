package com.example.compoint.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogDTO {
    private Long id;
    private String content;
    private UserDTO user;
    private LocalDateTime createdAt;
}
