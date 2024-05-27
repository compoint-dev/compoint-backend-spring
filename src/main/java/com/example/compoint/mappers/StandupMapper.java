package com.example.compoint.mappers;

import com.example.compoint.dtos.StandupDTO;
import com.example.compoint.entity.StandupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StandupMapper {
    StandupMapper INSTANCE = Mappers.getMapper(StandupMapper.class);

    StandupDTO standupEntityToStandupDTO(StandupEntity standup);
}
