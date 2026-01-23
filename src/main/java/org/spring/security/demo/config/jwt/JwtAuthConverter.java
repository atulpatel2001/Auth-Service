package org.spring.security.demo.config.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.security.demo.constant.SecurityConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/*
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();

        var roles = jwt.getClaimAsStringList(SecurityConstants.TOKEN_USER_ROLES);
        if (roles != null) {
            roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
        }

        var permissions = jwt.getClaimAsStringList(SecurityConstants.TOKEN_USER_PERMISSIONS);
        if (permissions != null) {
            permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));
        }

        return new JwtAuthenticationToken(jwt, authorities);
    }
}*/


@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthConverter.class.getName());



    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();

        try {
            // Extract roles from JWT claims
            List<String> roles = jwt.getClaimAsStringList(SecurityConstants.TOKEN_USER_ROLES);
            if (roles != null && !roles.isEmpty()) {
                roles.forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    logger.info("Added role: ROLE_{}", role);
                });
            }

            // Extract permissions from JWT claims
            List<String> permissions = jwt.getClaimAsStringList(SecurityConstants.TOKEN_USER_PERMISSIONS);
            if (permissions != null && !permissions.isEmpty()) {
                permissions.forEach(permission -> {
                    authorities.add(new SimpleGrantedAuthority(permission));
                    logger.info("Added permission: {}", permission);
                });
            }

            logger.info("JWT converted successfully for user: {} with authorities: {}", jwt.getSubject(), authorities.size());

            return new JwtAuthenticationToken(jwt, authorities);

        } catch (Exception e) {
            logger.error("Error converting JWT to authentication token: {}", e.getMessage());
            throw new RuntimeException("JWT conversion failed", e);
        }
    }
}
