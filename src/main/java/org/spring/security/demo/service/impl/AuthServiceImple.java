package org.spring.security.demo.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.spring.security.demo.constant.SecurityConstants;
import org.spring.security.demo.dto.*;
import org.spring.security.demo.exception.BusinessException;
import org.spring.security.demo.exception.ResourceNotFoundException;
import org.spring.security.demo.model.EPermission;
import org.spring.security.demo.model.redis.ERefreshToken;
import org.spring.security.demo.model.ERole;
import org.spring.security.demo.model.EUser;
import org.spring.security.demo.repository.redis.RefreshTokenRepository;
import org.spring.security.demo.repository.jpa.RoleRepository;
import org.spring.security.demo.repository.jpa.UserRepository;
import org.spring.security.demo.service.AuthService;
import org.spring.security.demo.util.ApplicationUtil;
import org.spring.security.demo.util.JwtUtil;
import org.spring.security.demo.util.SecurityContextUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImple  implements AuthService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;




    @Override
    public Long registerUser(RegisterUserRequest request) {

        if (userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
            throw new BusinessException("Email already registered",HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByPhoneNumberAndIsDeletedFalse(request.getPhoneNumber())) {
            throw new BusinessException("Phone number already registered",HttpStatus.BAD_REQUEST);
        }

        ERole defaultRole = roleRepository.findByNameAndIsDeletedFalse(SecurityConstants.DEFAULT_ROLE)
                .orElseThrow(() ->
                        new BusinessException("Default role USER not configured",HttpStatus.NOT_FOUND));

        EUser user = EUser.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdBy("SELF")
                .createdAt(LocalDateTime.now())
                .build();

        user.getRoles().add(defaultRole);

        return userRepository.save(user).getId();
    }


    @Override
    public AuthResponse login(LoginRequest request) {
        EUser user = userRepository.findByEmailOrPhoneNumberAndIsDeletedFalse(request.getDestination(), request.getDestination())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        return generateTokens(user);
    }


    @Override
    public AuthResponse refreshToken(String refreshTokenString, HttpServletRequest headerRequest) {

        if(!jwtUtil.validateJwtToken(refreshTokenString))
            throw new BusinessException("Refresh token is invalid", HttpStatus.UNAUTHORIZED);

        ERefreshToken refreshToken = refreshTokenRepository.findByTokenHash(refreshTokenString)
                .orElseThrow(() -> new BusinessException("Refresh token is invalid", HttpStatus.UNAUTHORIZED));

        if (refreshToken.getRevokedAt() != null || refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Refresh token is invalid", HttpStatus.UNAUTHORIZED);
        }

        EUser user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new BusinessException("Refresh token is invalid", HttpStatus.UNAUTHORIZED));



        return generateTokens(user);
    }

    @Override
    public void logout(HttpServletRequest headerRequest, String refreshToken) {
        if (!ApplicationUtil.isEmpty(refreshToken)) {
            refreshTokenRepository.findByTokenHash(refreshToken).ifPresent(refreshTokenRepository::delete);
        }

    }

    @Override
    public UserProfileResponse getUserProfile() {
        Long userId = SecurityContextUtil.getUserId();
        EUser user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<String> roles = user.getRoles().stream().map(ERole::getName).collect(Collectors.toSet());

        Set<String> permissions = user.getRoles().stream().flatMap(role -> role.getPermissions().stream())
                .map(EPermission::getCode).collect(Collectors.toSet());

        return new UserProfileResponse(user.getId(), user.getPhoneNumber(), user.getEmail(), roles, permissions);
    }


    private AuthResponse generateTokens(EUser user) {
        // Collect roles
        Set<String> roles = user.getRoles().stream().map(ERole::getName).collect(Collectors.toSet());

        Set<String> permissions = user.getRoles().stream().flatMap(role -> role.getPermissions().stream())
                .map(EPermission::getCode).collect(Collectors.toSet());

        UUID tokenId = UUID.randomUUID();

        // Generate tokens
        String accessTokenString = jwtUtil.generate(user, SecurityConstants.TOKEN_ACCESS,tokenId);
        String refreshTokenString = jwtUtil.generate(user, SecurityConstants.TOKEN_REFRESH, null);

        ERefreshToken refreshToken = ERefreshToken.builder().userId(user.getId())
                .tokenHash(refreshTokenString).expiresAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .createdBy("SYSTEM").build();

        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessTokenString, refreshTokenString, SecurityConstants.BREAR,
                new AuthUserDto(user.getId(), user.getEmail(), user.getPhoneNumber(), roles, permissions));
    }




}
