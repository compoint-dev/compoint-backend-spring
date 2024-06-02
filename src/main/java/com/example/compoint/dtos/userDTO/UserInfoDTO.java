package com.example.compoint.dtos.userDTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {
    private String imagePath;
    private Long age;
}
