package com.example.compoint.mappers;

import com.example.compoint.dtos.userDTO.UserInfoDTO;
import com.example.compoint.entity.UserInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserInfoMapper {
    UserInfoMapper INSTANCE = Mappers.getMapper(UserInfoMapper.class);


    UserInfoDTO userInfoEntityToUserInfoDTO(UserInfoEntity userInfoEntity);
    UserInfoEntity userInfoDTOToUserInfoEntity(UserInfoDTO userInfoDTO);
    void updateUserInfoFromDTO(UserInfoDTO userInfoDTO, @MappingTarget UserInfoEntity userInfoEntity);

}
