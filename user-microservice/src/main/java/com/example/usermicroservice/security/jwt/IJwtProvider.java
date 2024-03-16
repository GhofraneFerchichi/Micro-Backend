package com.example.usermicroservice.security.jwt;

import com.example.usermicroservice.security.UserPrincipal;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface IJwtProvider {
    String generateToken(UserPrincipal auth);
    boolean validateToken(HttpServletRequest request);
    Authentication getAuthentication(HttpServletRequest request);

}
