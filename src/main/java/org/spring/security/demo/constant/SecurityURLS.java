package org.spring.security.demo.constant;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.List;

public class SecurityURLS {


    private SecurityURLS() {}

    public static final List<String> PUBLIC_PATHS = List.of(
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/api/auth/**"

    );

    public static boolean isPublic(String path) {
        return PUBLIC_PATHS.stream().anyMatch(p -> pathMatcher(path, p));
    }

    private static boolean pathMatcher(String path, String pattern) {
        PathPatternParser parser = new PathPatternParser();
        return parser.parse(pattern).matches(PathContainer.parsePath(path));
    }
}
