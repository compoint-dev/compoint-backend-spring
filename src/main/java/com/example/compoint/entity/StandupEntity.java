package com.example.compoint.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
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
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Auto-generated ID of the Standup")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToOne(mappedBy = "standup", cascade = CascadeType.ALL, orphanRemoval = true)
    private StandupInfoEntity standupInfo;

    @OneToMany(mappedBy = "standup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentEntity> comments;

    @OneToMany(mappedBy = "standup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WatchLaterEntity> watchLaterList;
}