package com.example.compoint.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StandupInfoDTO {

//    private Long id;

    private Integer rating;

    private String genre;

    private String pg;

    private Long views;

    private String imagePath;


    private LocalDateTime createdAt;

}
