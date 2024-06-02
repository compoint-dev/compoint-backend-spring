package com.example.compoint.mappers;

import com.example.compoint.dtos.userDTO.UserDTO;
import com.example.compoint.dtos.userDTO.UserSignupRequest;
import com.example.compoint.dtos.userDTO.UserUpdateResponse;
import com.example.compoint.dtos.userDTO.UserWithoutPasswordDTO;
import com.example.compoint.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserInfoMapper.class, RoleMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userEntityToUserDTO(UserEntity userEntity);

    UserEntity userSignupRequestToUserEntity(UserSignupRequest userSignupRequest);
    UserWithoutPasswordDTO userEntityToUserWithoutPasswordDTO(UserEntity userEntity);
    UserUpdateResponse userEntityToUserUpdateResponse(UserEntity userEntity);

}