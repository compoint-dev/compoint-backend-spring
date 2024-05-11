package com.example.compoint.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "standupinfo")
public class StandupInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Auto-generated ID of the WatchLater")
    private Long id;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Rating are assigned internally, not provided by client")
    private Integer rating;

    private String genre;

    private String pg;

    private Long views;

    private String imagePath;

    @ManyToMany
    @JoinTable(
            name = "standup_languages",
            joinColumns = @JoinColumn(name = "standup_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<LanguageEntity> languages;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "standup_id", unique = true, nullable = false)
    private StandupEntity standup;
}
