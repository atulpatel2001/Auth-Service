package org.spring.security.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.security.demo.dto.*;
import org.spring.security.demo.service.AuthService;
import org.spring.security.demo.service.UserService;
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



    @PostMapping("/register")
    public ApiResponse<Long> register(
            @Valid @RequestBody ApiRequest<RegisterUserRequest> request) {

        return ApiResponse.<Long>builder()
                .success(true)
                .message("User registered successfully")
                .data(authService.registerUser(request.getData()))
                .build();
    }


    @PostMapping("/login")
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



}

