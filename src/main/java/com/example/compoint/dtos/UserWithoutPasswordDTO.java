package com.example.compoint.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithoutPasswordDTO {
    private Long id;
    private String username;
    private String email;
    private Set<RoleDTO> roles;
    private UserInfoDTO userInfo;
}