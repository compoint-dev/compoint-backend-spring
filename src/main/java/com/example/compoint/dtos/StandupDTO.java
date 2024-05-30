package com.example.compoint.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StandupDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private UserDTO user;
    private StandupInfoDTO standupInfo;
}

