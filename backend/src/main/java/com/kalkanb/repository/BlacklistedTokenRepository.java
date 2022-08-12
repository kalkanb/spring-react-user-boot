package com.kalkanb.repository;

import com.kalkanb.entity.BlacklistedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedTokenEntity, Long> {
    Optional<BlacklistedTokenEntity> findByToken(String token);

    List<BlacklistedTokenEntity> findAllByExpirationDateBefore(LocalDateTime expirationDateLimit);
}
