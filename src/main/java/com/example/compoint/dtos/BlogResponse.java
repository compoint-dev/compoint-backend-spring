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
public class BlogResponse {
    private Long id;
    private String content;
    private UserSummaryDTO user;
    private LocalDateTime createdAt;
}
