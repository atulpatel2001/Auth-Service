package org.spring.security.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.security.demo.dto.*;
import org.spring.security.demo.service.AuthService;
import org.spring.security.demo.util.CookieUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ApiResponse<Long> register(
            @Valid @RequestBody ApiRequest<RegisterUserRequest> request) {

        return ApiResponse.<Long>builder()
                .success(true)
                .message("User registered successfully")
                .data(authService.registerUser(request.getData()))
                .build();
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ApiResponse<AuthUserDto> login(@Valid @RequestBody ApiRequest<LoginRequest> request,
            HttpServletResponse response) {

        AuthResponse auth = authService.login(request.getData());
        logger.info("User Login successfully");
        cookieUtil.setCookies(response, auth.getAccessToken(), auth.getRefreshToken());
        return ApiResponse.<AuthUserDto>builder()
                .success(true)
                .message("User Login successfully")
                .data(auth.getUser())
                .build();
    }

    // ================= REFRESH =================
    @PostMapping("/refresh")
    public ApiResponse<AuthUserDto> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        AuthResponse auth = authService.refreshToken(cookieUtil.extractRefreshToken(request));
        cookieUtil.setCookies(response, auth.getAccessToken(), auth.getRefreshToken());

        return ApiResponse.<AuthUserDto>builder()
                .success(true)
                .message("Access token generated successfully.")
                .data(auth.getUser())
                .build();
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        String refreshToken = cookieUtil.extractRefreshToken(request);
        authService.logout(refreshToken);
        cookieUtil.clearCookies(response);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("User logged out successfully.")
                .data(null)
                .build();
    }
}
