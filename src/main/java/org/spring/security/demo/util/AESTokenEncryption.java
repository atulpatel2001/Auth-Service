package org.spring.security.demo.util;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@Component
public class AESTokenEncryption {

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;

    @Value("${aes.secret.key}")
    private String aesSecretKeyString;

    private SecretKey secretKey;

    // Initialize secret key from properties
    public void init() {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(aesSecretKeyString);
            this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid AES secret key format", e);
        }
    }

    // Generate new AES key (run once and store in properties)
    public static String generateNewAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE, new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // Encrypt JWT token
    public String encryptToken(String jwtToken) throws Exception {
        if (secretKey == null) {
            init();
        }

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedToken = cipher.doFinal(jwtToken.getBytes());
        return Base64.getEncoder().encodeToString(encryptedToken);
    }

    // Decrypt JWT token
    public String decryptToken(String encryptedToken) throws Exception {
        if (secretKey == null) {
            init();
        }

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedToken = Base64.getDecoder().decode(encryptedToken);
        byte[] decryptedToken = cipher.doFinal(decodedToken);
        return new String(decryptedToken);
    }
}
