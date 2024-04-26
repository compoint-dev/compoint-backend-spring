package com.example.compoint.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "languages")
public class LanguageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // Например, 'English', 'Spanish' и т.д.

    @ManyToMany(mappedBy = "languages")
    private Set<StandupEntity> standups;
}
