package org.spring.security.demo.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.security.demo.constant.SecurityConstants;
import org.spring.security.demo.dto.AuthResponse;
import org.spring.security.demo.exception.BusinessException;
import org.spring.security.demo.model.ERole;
import org.spring.security.demo.model.EUser;
import org.spring.security.demo.repository.jpa.RoleRepository;
import org.spring.security.demo.repository.jpa.UserRepository;
import org.spring.security.demo.service.TokenService;
import org.spring.security.demo.util.CookieUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final CookieUtil cookieUtil;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        String provider = oauthToken.getAuthorizedClientRegistrationId().toUpperCase();

        // Extract email — fallback to login@github.com for GitHub
        String email = extractEmail(oAuth2User);

        logger.info("OAuth2 login success: provider={}, email={}", provider, email);

        // Find or create user
        EUser user = findOrCreateUser(email, provider);

        // Generate tokens and set cookies
        AuthResponse auth = tokenService.generateTokens(user);
        cookieUtil.setCookies(response, auth.getAccessToken(), auth.getRefreshToken());

        // Redirect to frontend
        response.sendRedirect(frontendUrl + "/dashboard");
    }

    private String extractEmail(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("email") != null
                ? oAuth2User.getAttribute("email").toString()
                : oAuth2User.getAttribute("login").toString() + "@github.com";
    }

    private EUser findOrCreateUser(String email, String provider) {
        Optional<EUser> existingUser = userRepository.findByEmailWithRolesAndIsDeletedFalse(email);

        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        ERole defaultRole = roleRepository.findByNameAndIsDeletedFalse(SecurityConstants.DEFAULT_ROLE)
                .orElseThrow(() -> new BusinessException("Default role USER not configured", HttpStatus.NOT_FOUND));

        EUser user = EUser.builder()
                .email(email)
                .createdBy(provider)
                .createdAt(LocalDateTime.now())
                .build();
        user.getRoles().add(defaultRole);
        return userRepository.save(user);
    }
}
