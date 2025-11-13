package com.github.playlistService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.playlistService.model.acceso;

@Repository
public interface accesoRepository  extends JpaRepository<acceso,Long>{

}
