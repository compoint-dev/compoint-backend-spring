package com.example.compoint.dtos;

import com.example.compoint.entity.StandupEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    private String imagePath;
    private Integer rating;
    private UserDTO user;
    private Set<LanguageDTO> languages;

    // toDTO
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

    public StandupDTO(Optional<StandupEntity> standup) {
        this.id = standup.get().getId();
        this.name = standup.get().getName();
        this.description = standup.get().getDescription();
        this.price = standup.get().getPrice();
        this.imagePath = standup.get().getImagePath();
        this.rating = standup.get().getRating();
        this.user = new UserDTO(standup.get().getUser().getUsername());
        this.languages = standup.get().getLanguages().stream()
                .map(language -> new LanguageDTO(language.getName()))
                .collect(Collectors.toSet());
    }
}

