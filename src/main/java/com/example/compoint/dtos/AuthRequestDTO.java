package com.example.compoint.dtos;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestDTO {
    private String username;
    private String password;
}