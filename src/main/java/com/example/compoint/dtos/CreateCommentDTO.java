package com.example.compoint.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentDTO {
    private String comment;
}