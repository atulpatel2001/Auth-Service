package org.spring.security.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.security.demo.constant.SecurityConstants;
import org.spring.security.demo.dto.AuthResponse;
import org.spring.security.demo.dto.AuthUserDto;
import org.spring.security.demo.model.EPermission;
import org.spring.security.demo.model.ERole;
import org.spring.security.demo.model.EUser;
import org.spring.security.demo.model.redis.ERefreshToken;
import org.spring.security.demo.repository.redis.RefreshTokenRepository;
import org.spring.security.demo.service.TokenService;
import org.spring.security.demo.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public AuthResponse generateTokens(EUser user) {
        Set<String> roles = user.getRoles().stream().map(ERole::getName).collect(Collectors.toSet());

        Set<String> permissions = user.getRoles().stream().flatMap(role -> role.getPermissions().stream())
                .map(EPermission::getCode).collect(Collectors.toSet());

        UUID tokenId = UUID.randomUUID();

        String accessTokenString = jwtUtil.generate(user, SecurityConstants.TOKEN_ACCESS, tokenId);
        String refreshTokenString = jwtUtil.generate(user, SecurityConstants.TOKEN_REFRESH, null);

        ERefreshToken refreshToken = ERefreshToken.builder()
                .userId(user.getId())
                .tokenHash(refreshTokenString)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .createdBy("SYSTEM")
                .build();

        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessTokenString, refreshTokenString, SecurityConstants.BREAR,
                new AuthUserDto(user.getId(), user.getEmail(), user.getPhoneNumber(), roles, permissions));
    }
}
