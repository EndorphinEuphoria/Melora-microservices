package com.github.recoverpass_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.recoverpass_service.model.ResetToken;
import java.util.Optional;


@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long>{
    Optional<ResetToken> findByToken(String token);
}
