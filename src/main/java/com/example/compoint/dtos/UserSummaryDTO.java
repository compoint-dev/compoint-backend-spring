package com.example.compoint.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSummaryDTO {
    private Long id;
    private String username;
}
