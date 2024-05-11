package com.example.compoint.dtos;

import com.example.compoint.entity.LanguageEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class StandupInfoDTO {

//    private Long id;

    private Integer rating;

    private String genre;

    private String pg;

    private Long views;

    private String imagePath;

    private Set<LanguageDTO> languages;
}
