package com.example.compoint.dtos;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<RoleDTO> roles;
    private UserInfoDTO userInfo;
}
