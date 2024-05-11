package com.example.compoint.dtos;

import com.example.compoint.entity.StandupEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class StandupDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private UserDTO user;
    private LocalDateTime createdAt;
    private StandupInfoDTO standupInfo;
}

