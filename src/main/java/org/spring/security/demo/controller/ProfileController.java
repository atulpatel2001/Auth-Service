package org.spring.security.demo.controller;

import lombok.RequiredArgsConstructor;
import org.spring.security.demo.dto.ApiResponse;
import org.spring.security.demo.dto.UserProfileResponse;
import org.spring.security.demo.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor

public class ProfileController {

    private final AuthService authService;

    @PostMapping("/profile")
    public ApiResponse<UserProfileResponse> getCurrentUser() {
        return ApiResponse.<UserProfileResponse>builder()
                .success(true)
                .message("User profile retrieved successfully")
                .data(authService.getUserProfile())
                .build();
    }
}
