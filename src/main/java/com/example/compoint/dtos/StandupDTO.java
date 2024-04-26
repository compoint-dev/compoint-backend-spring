package com.example.compoint.dtos;

import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.entity.LanguageEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class StandupDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imagePath;
    private Integer rating;
    private UserDTO user;
    private Set<LanguageDTO> languages;

    // Пустой конструктор
    public StandupDTO() {
    }

    // Конструктор для конвертации из сущности
    public StandupDTO(StandupEntity standup) {
        this.id = standup.getId();
        this.name = standup.getName();
        this.description = standup.getDescription();
        this.price = standup.getPrice();
        this.imagePath = standup.getImagePath();
        this.rating = standup.getRating();
        this.user = new UserDTO(standup.getUser().getUsername());
        this.languages = standup.getLanguages().stream()
                .map(language -> new LanguageDTO(language.getName()))
                .collect(Collectors.toSet());
    }
}

