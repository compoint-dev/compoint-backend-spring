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
    private String imagePath;
    private Integer rating;
    private UserDTO user;
    private Set<LanguageDTO> languages;
    private LocalDateTime createdAt;

    // toDTO
    public StandupDTO(StandupEntity standupEntity) {
        this.id = standupEntity.getId();
        this.name = standupEntity.getName();
        this.description = standupEntity.getDescription();
        this.price = standupEntity.getPrice();
        this.imagePath = standupEntity.getImagePath();
        this.rating = standupEntity.getRating();
        this.user = new UserDTO(standupEntity.getUser().getUsername());
        this.createdAt = standupEntity.getCreatedAt();
        this.languages = standupEntity.getLanguages().stream()
                .map(language -> new LanguageDTO(language.getName()))
                .collect(Collectors.toSet());
    }

    public StandupDTO(Optional<StandupEntity> standupEntity) {
        this.id = standupEntity.get().getId();
        this.name = standupEntity.get().getName();
        this.description = standupEntity.get().getDescription();
        this.price = standupEntity.get().getPrice();
        this.imagePath = standupEntity.get().getImagePath();
        this.rating = standupEntity.get().getRating();
        this.user = new UserDTO(standupEntity.get().getUser().getUsername());
        this.createdAt = standupEntity.get().getCreatedAt();
        this.languages = standupEntity.get().getLanguages().stream()
                .map(language -> new LanguageDTO(language.getName()))
                .collect(Collectors.toSet());
    }
}

