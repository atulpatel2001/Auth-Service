package org.spring.security.demo.util;

public final class SecurityTextUtil {

    private SecurityTextUtil() {}

    public static String normalize(String value) {
        return value
                .trim()
                .replaceAll("\\s+", "_")
                .toUpperCase();
    }
}

