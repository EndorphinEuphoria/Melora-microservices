package com.github.music_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFavorite;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long songId;
}
