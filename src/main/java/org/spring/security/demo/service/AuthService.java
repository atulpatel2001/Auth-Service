package org.spring.security.demo.service;

import jakarta.servlet.http.HttpServletRequest;
import org.spring.security.demo.dto.AuthResponse;
import org.spring.security.demo.dto.LoginRequest;
import org.spring.security.demo.dto.RegisterUserRequest;
import org.spring.security.demo.dto.UserProfileResponse;


public interface AuthService {

    AuthResponse login(LoginRequest dto);

    Long registerUser(RegisterUserRequest request);
    AuthResponse refreshToken(String refreshTokenString, HttpServletRequest headerRequest);

    void logout(HttpServletRequest headerRequest, String refreshToken);

    UserProfileResponse getUserProfile();
}
