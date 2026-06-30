package org.spring.security.demo.service;

import org.spring.security.demo.dto.AuthResponse;
import org.spring.security.demo.model.EUser;

public interface TokenService {

    AuthResponse generateTokens(EUser user);
}
