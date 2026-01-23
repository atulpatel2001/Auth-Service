package org.spring.security.demo.model.redis;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("access_tokens")
public class ERefreshToken implements Serializable {

	@Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private Long userId;
//	private String sessionId;
	private String tokenHash;
    private LocalDateTime expiresAt;
    private LocalDateTime revokedAt;
    private String createdBy;
    private LocalDateTime createdAt;
    /*private String deviceId;
    private String ipAddress;
    private String location;
    private String userAgent;*/

}
