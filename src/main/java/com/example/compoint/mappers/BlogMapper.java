package com.example.compoint.mappers;

import com.example.compoint.dtos.BlogDTO;
import com.example.compoint.entity.BlogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlogMapper {
    BlogMapper INSTANCE = Mappers.getMapper(BlogMapper.class);

    BlogDTO blogEntityToBlogDTO(BlogEntity blog);
}
