package com.example.compoint.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long id;

    private String username;

    private String email;

    private Set<RoleDTO> roles;

    private UserInfoDTO userInfo;
}
