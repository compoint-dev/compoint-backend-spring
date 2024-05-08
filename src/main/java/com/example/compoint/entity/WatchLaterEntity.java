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
@Table(name = "watchlater")
public class WatchLaterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Auto-generated ID of the WatchLater")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "standup_id", nullable = false)
    private StandupEntity standup;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false, updatable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "CreatedAt are assigned internally, not provided by client")
    private LocalDateTime addedAt;
}
