package org.spring.security.demo.config.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtDecoderConfig {
	
	private final String PUBLIC_PEM_FILE = "public_key.pem";
	private final String RSA = "RSA";

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        // Load public key from PEM
        String keyContent = new String(Files.readAllBytes(
                new ClassPathResource(PUBLIC_PEM_FILE).getFile().toPath()
        ))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(keyContent));
        KeyFactory kf = KeyFactory.getInstance(RSA);
        RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(spec);

        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}
