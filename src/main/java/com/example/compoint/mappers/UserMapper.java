package com.example.compoint.mappers;

import com.example.compoint.dtos.RoleDTO;
import com.example.compoint.dtos.UserDTO;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.UserEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(uses = {UserInfoMapper.class, RoleMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userEntityToUserDTO(UserEntity user);

}