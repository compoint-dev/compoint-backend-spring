package com.example.compoint.mappers;

import com.example.compoint.dtos.StandupInfoDTO;
import com.example.compoint.dtos.UserInfoDTO;
import com.example.compoint.entity.StandupInfoEntity;
import com.example.compoint.entity.UserInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StandupInfoMapper {
    StandupInfoMapper INSTANCE = Mappers.getMapper(StandupInfoMapper.class);

    StandupInfoDTO standupInfoEntityToStandupInfoDTO(StandupInfoEntity standupInfo);
}
