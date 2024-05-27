package com.example.compoint.mappers;

import com.example.compoint.dtos.UserDTO;
import com.example.compoint.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserInfoMapper.class, RoleMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userEntityToUserDTO(UserEntity user);

}