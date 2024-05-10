package com.example.compoint.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Auto-generated ID of the User")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private BigDecimal price;

    private String imagePath;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Rating are assigned internally, not provided by client")
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToMany
    @JoinTable(
            name = "standup_languages",
            joinColumns = @JoinColumn(name = "standup_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<LanguageEntity> languages;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "CreatedAt are assigned internally, not provided by client")
    private LocalDateTime createdAt;

}
