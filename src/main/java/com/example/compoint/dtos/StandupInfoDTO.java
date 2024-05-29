package com.example.compoint.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class StandupInfoDTO {

    private Integer rating;

    private String genre;

    private String pg;

    private Long views;

    private String imagePath;

    private Set<LanguageDTO> languages;

    private LocalDateTime createdAt;

}
