/*
package org.spring.security.demo.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.spring.security.demo.constant.SecurityURLS;
import org.spring.security.demo.enums.HttpStatus;
import org.spring.security.demo.util.AESTokenEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Component
public class JwtRevocationFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;
    private final JwtDecoder jwtDecoder;
    private final String AUTHORIZATION = "Authorization";
    private final String BEARER = "Bearer ";
    private final String ID = "id";
    private final String ACCESS_TOKENS= "access_tokens";

	@Autowired
	private AESTokenEncryption aesTokenEncryption;

    public JwtRevocationFilter(StringRedisTemplate redisTemplate, JwtDecoder jwtDecoder) {
        this.redisTemplate = redisTemplate;
        this.jwtDecoder = jwtDecoder;
    }
*/
/*
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

	        String authHeader = request.getHeader(AUTHORIZATION);
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
	            String encryptedToken = authHeader.substring(7).trim();


         try {
			        String token= decryptToken(encryptedToken);
	                Jwt jwt = jwtDecoder.decode(token);

	                if(jwt.getExpiresAt().isBefore(Instant.now())) {
	                	response.setStatus(HttpStatus.TOKEN_EXPIRED.value());
	                	return;
	                }

	            }catch (JwtException e) {
	                response.setStatus(HttpStatus.UNAUTHORIZED.value());
	                return;
	            }catch (Exception e) {
			 	response.setStatus(HttpStatus.UNAUTHORIZED.value());
		 }
        filterChain.doFilter(request, response);
    }


	private String decryptToken(String encryptedToken) throws Exception {
		return aesTokenEncryption.decryptToken(encryptedToken);
	}*//*



	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader(AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith(BEARER)) {
			filterChain.doFilter(request, response);
			return;
		}

		String encryptedToken = authHeader.substring(7).trim();

		try {
			// Step 1: Decrypt the AES-encrypted token
			String decryptedToken = decryptToken(encryptedToken);
			logger.info("Token decrypted successfully");

			// Step 2: Decode and validate the JWT
			Jwt jwt = jwtDecoder.decode(decryptedToken);
			logger.info("JWT decoded successfully");

			// Step 3: Check if token is expired
			if (jwt.getExpiresAt() != null && jwt.getExpiresAt().isBefore(Instant.now())) {
				response.setStatus(HttpStatus.TOKEN_EXPIRED.value());
				response.getWriter().write("Token has expired");
				return;
			}

			logger.info("Token validation successful for user: " + jwt.getSubject());

		} catch (Exception e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write("Unauthorized: " + e.getMessage());
			return;
		}

		filterChain.doFilter(request, response);
	}

	*/
/**
	 * Decrypt the AES-encrypted token
	 *//*

	private String decryptToken(String encryptedToken) throws Exception {
		if (encryptedToken == null || encryptedToken.isEmpty()) {
			throw new IllegalArgumentException("Encrypted token is null or empty");
		}

		try {
			return aesTokenEncryption.decryptToken(encryptedToken);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid token format: unable to decode Base64", e);
		} catch (Exception e) {
			throw new Exception("AES decryption failed: " + e.getMessage(), e);
		}
	}
}

*/
