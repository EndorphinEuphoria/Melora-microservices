package com.github.playlistService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "acceso")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class acceso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAcceso;
    
    @Column(nullable = false)
    private String nombre; 
}
