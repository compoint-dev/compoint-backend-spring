package com.example.compoint.dtos;

import com.example.compoint.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequestDTO {

    private Long id;
    private String username;
    private String password;
    private Set<RoleEntity> roles;

}