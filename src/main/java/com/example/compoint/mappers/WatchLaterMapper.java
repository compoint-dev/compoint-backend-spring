package com.example.compoint.mappers;

import com.example.compoint.dtos.WatchLaterDTO;
import com.example.compoint.entity.WatchLaterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WatchLaterMapper {
    WatchLaterMapper INSTANCE = Mappers.getMapper(WatchLaterMapper.class);

    WatchLaterDTO watchLaterEntityToWatchLaterDTO(WatchLaterEntity watchLater);
}
