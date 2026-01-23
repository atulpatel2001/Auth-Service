package org.spring.security.demo.util;


import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.spring.security.demo.constant.SecurityConstants;
import org.spring.security.demo.model.EPermission;
import org.spring.security.demo.model.ERole;
import org.spring.security.demo.model.EUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private String jwtAccessExpirationTime;

    @Value("${jwt.refresh.expiration}")
    private String jwtRefreshExpirationTime;

    private PrivateKey privateKey;

    @Autowired
    private AESTokenEncryption tokenEncryption;

    public JwtUtil() throws Exception {
        String keyContent = new String(Files.readAllBytes(new ClassPathResource("private_key.pem").getFile().toPath()))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyContent));
        this.privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(privateKey).build().parseClaimsJws(token).getBody();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generate(EUser user, String type, UUID id) {
        Map<String, Object> claims = new HashMap<>();
        if(!ApplicationUtil.isEmpty(id))
            claims.put(SecurityConstants.TOKEN_ID, id);
        claims.put(SecurityConstants.TOKEN_USER_ID, user.getId());
        claims.put(SecurityConstants.TOKEN_USER_EMAIL, user.getEmail());
        claims.put(SecurityConstants.TOKEN_USER_PHONE_NUMBER, user.getPhoneNumber());
        Set<String> roles = user.getRoles()
                .stream()
                .map(ERole::getName)
                .collect(Collectors.toSet());
        claims.put(SecurityConstants.TOKEN_USER_ROLES, roles);

        Set<String> permissions = user.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(EPermission::getCode)
                .collect(Collectors.toSet());
        claims.put(SecurityConstants.TOKEN_USER_PERMISSIONS, permissions);

        return doGenerateToken(claims, user.getEmail(), type);
    }

    private String doGenerateToken(Map<String, Object> claims, String username, String type) {
        long expirationTimeLong;
        if (SecurityConstants.TOKEN_ACCESS.equals(type)) {
            expirationTimeLong = Long.parseLong(jwtAccessExpirationTime) * 1000;
        } else {
            expirationTimeLong = Long.parseLong(jwtRefreshExpirationTime) * 1000 ;
        }
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);

        String jwtToken= Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(privateKey, SignatureAlgorithm.RS256).compact();
        try {
            return tokenEncryption.encryptToken(jwtToken);
        } catch (Exception e) {
            throw new RuntimeException("Token encryption failed", e);
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            String authTokenDecrypted = tokenEncryption.decryptToken(authToken);
            Jwts.parserBuilder().setSigningKey(privateKey).build().parseClaimsJws(authTokenDecrypted);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: {}" + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: {}" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: {}" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: {}" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid JWT token: {}" + e.getMessage());
        }

        return false;
    }

}
