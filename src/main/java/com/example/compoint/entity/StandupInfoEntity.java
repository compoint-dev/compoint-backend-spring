package com.example.compoint.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "standupinfo")
public class StandupInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Auto-generated ID of the StandupInfo")
    private Long id;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Rating are assigned internally, not provided by client")
    private Integer rating;

    private String genre;

    private String pg;

    private Long views;

    private String imagePath;

    @OneToOne
    @JoinColumn(name = "standup_id", referencedColumnName = "id", nullable = false)
    private StandupEntity standup;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}