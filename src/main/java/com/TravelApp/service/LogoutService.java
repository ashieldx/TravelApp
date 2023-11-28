package com.TravelApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.TravelApp.entity.Token;
import com.TravelApp.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LogoutService implements LogoutHandler {
    private final String AUTH_HEADER = "Authorization";
    private final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(AUTH_HEADER);
        final String jwt;

        if(authHeader == null || !authHeader.startsWith(BEARER_PREFIX)){
            return;
        }

        jwt = authHeader.substring(7);
        Token token = tokenRepository.findByToken(jwt).orElse(null);

        if(token != null){
            token.setExpired(true);
            tokenRepository.save(token);
        }

    }
    
}
