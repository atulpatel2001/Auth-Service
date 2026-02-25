package org.spring.security.demo.config.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.spring.security.demo.constant.SecurityConstants;
import org.spring.security.demo.constant.SecurityURLS;
import org.spring.security.demo.util.AESTokenEncryption;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BearerTokenResolverConfig implements BearerTokenResolver {

    private final AESTokenEncryption aesTokenEncryption;



    /* this is create for token get from header
    */

   /* @Override
    public String resolve(HttpServletRequest request) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String encryptedToken = authHeader.substring(7).trim();

        try {
            //  AES decrypt BEFORE Spring sees it
            return aesTokenEncryption.decryptToken(encryptedToken);
        } catch (Exception e) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_token", "Invalid AES token", null)
            );
        }
    }*/


    @Override
    public String resolve(HttpServletRequest request) {

        if (request.getCookies() == null) return null;
        if(SecurityURLS.isPublic(request.getRequestURI())){
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if (SecurityConstants.ACCESS_TOKEN.equals(cookie.getName())) {
                try {
                    return aesTokenEncryption.decryptToken(cookie.getValue());
                } catch (Exception e) {
                    throw new OAuth2AuthenticationException(
                            new OAuth2Error("invalid_token", "Invalid AES token", null)
                    );
                }
            }
        }

        return null;
    }
}