package com.example.compoint.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "standups")
public class StandupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imagePath;
    private Integer rating;

    @ManyToMany
    @JoinTable(
            name = "standup_languages",
            joinColumns = @JoinColumn(name = "standup_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<LanguageEntity> languages;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
