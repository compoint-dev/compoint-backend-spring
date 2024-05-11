package com.example.compoint.mappers;

import com.example.compoint.dtos.UserInfoDTO;
import com.example.compoint.entity.UserInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserInfoMapper {
    UserInfoMapper INSTANCE = Mappers.getMapper(UserInfoMapper.class);

    UserInfoDTO userInfoEntityToUserInfoDTO(UserInfoEntity userInfo);
}
