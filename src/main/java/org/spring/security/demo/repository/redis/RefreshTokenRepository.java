package org.spring.security.demo.repository.redis;

import org.spring.security.demo.model.redis.ERefreshToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends CrudRepository<ERefreshToken, UUID> {

    Optional<ERefreshToken> findByTokenHash(String tokenHash);
    void deleteByTokenHash(String tokenHash);

    @Query("SELECT t FROM ERefreshToken t " +
            "WHERE " +
            "t.expiresAt > CURRENT_TIMESTAMP")
    List<ERefreshToken> findActiveTokens();

    @Query("SELECT t FROM ERefreshToken t " +
            "WHERE " +
            "t.expiresAt > CURRENT_TIMESTAMP AND t.userId = :userId")
    List<ERefreshToken> findActiveTokensBYUserId(Long userId);
}
