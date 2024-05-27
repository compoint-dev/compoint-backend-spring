package com.example.compoint.mappers;

import com.example.compoint.dtos.LanguageDTO;
import com.example.compoint.entity.LanguageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LanguageMapper {
    LanguageMapper INSTANCE = Mappers.getMapper(LanguageMapper.class);

    LanguageDTO languageEntityToLanguageDTO(LanguageEntity language);
}
