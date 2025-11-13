package com.github.playlistService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.playlistService.model.categoria;

@Repository
public interface categoriaRepository extends JpaRepository<categoria, Long> {

}
