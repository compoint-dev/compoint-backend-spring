package com.example.compoint.dtos;

import io.swagger.v3.oas.annotations.Parameter;
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
    private String password;
    private Set<RoleDTO> roles;
    private UserInfoDTO userInfo;
}
