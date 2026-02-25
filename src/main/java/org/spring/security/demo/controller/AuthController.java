package org.spring.security.demo.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.security.demo.constant.SecurityConstants;
import org.spring.security.demo.dto.*;
import org.spring.security.demo.service.AuthService;
import org.spring.security.demo.util.AESTokenEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AESTokenEncryption tokenEncryption;

    @PostMapping("/register")
    public ApiResponse<Long> register(
            @Valid @RequestBody ApiRequest<RegisterUserRequest> request) {

        return ApiResponse.<Long>builder()
                .success(true)
                .message("User registered successfully")
                .data(authService.registerUser(request.getData()))
                .build();
    }


    /*@PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody ApiRequest<LoginRequest> request) {
        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("User Login successfully")
                .data(authService.login(request.getData()))
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthResponse> refreshToken(@Valid @RequestBody ApiRequest<AuthRequest> request, HttpServletRequest headerRequest) {
        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Access token generated successfully.")
                .data(authService.refreshToken(request.getData().getRefreshToken(),headerRequest))
                .build();
    }


    @PostMapping("/logout")
    public ApiResponse<AuthResponse> logout(@Valid @RequestBody ApiRequest<AuthRequest> request, HttpServletRequest headerRequest) {
        authService.logout(headerRequest,request.getData().getRefreshToken());
        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("User logged out successfully.")
                .data(null)
                .build();
    }
*/

    

    // ================= LOGIN =================
    @PostMapping("/login")
    public ApiResponse<AuthUserDto> login(@Valid @RequestBody ApiRequest<LoginRequest> request,
            HttpServletResponse response) {

        AuthResponse auth = authService.login(request.getData());

        setCookies(response, auth.getAccessToken(), auth.getRefreshToken());
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
            HttpServletResponse response){


        AuthResponse auth = authService.refreshToken(extractCookie(request));
        setCookies(response, auth.getAccessToken(), auth.getRefreshToken());

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
            HttpServletResponse response){

        String refreshToken = extractCookie(request);

        authService.logout(refreshToken);

        clearCookies(response);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("User logged out successfully.")
                .data(null)
                .build();
    }

    // ================= COOKIE METHODS =================

    private void setCookies(HttpServletResponse response,
                            String accessToken,
                            String refreshToken) {

        ResponseCookie accessCookie = ResponseCookie.from(SecurityConstants.ACCESS_TOKEN, accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(SecurityConstants.REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    private void clearCookies(HttpServletResponse response) {

        ResponseCookie deleteAccess = ResponseCookie.from(SecurityConstants.ACCESS_TOKEN, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        ResponseCookie deleteRefresh = ResponseCookie.from(SecurityConstants.REFRESH_TOKEN, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefresh.toString());
    }

    private String extractCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (SecurityConstants.REFRESH_TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}

