package com.example.compoint.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class StandupDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private UserDTO user;
    private StandupInfoDTO standupInfo;
}

