package com.theshireofpaws.security.filter;

import com.theshireofpaws.dto.request.AdminLoginRequest;
import com.theshireofpaws.dto.response.JwtResponse;
import com.theshireofpaws.security.CustomAuthenticationManager;
import com.theshireofpaws.security.SecurityConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private final CustomAuthenticationManager authenticationManager;
    
    public JWTAuthenticationFilter(CustomAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(SecurityConstants.LOGIN_URL);
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, 
                                               HttpServletResponse response) 
                                               throws AuthenticationException {
        try {
            AdminLoginRequest credentials = new ObjectMapper()
                .readValue(request.getInputStream(), AdminLoginRequest.class);
            
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                credentials.getEmail(),
                credentials.getPassword()
            );
            
            return authenticationManager.authenticate(authentication);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request", e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                           HttpServletResponse response, 
                                           FilterChain chain, 
                                           Authentication authResult) 
                                           throws IOException, ServletException {
        
        String token = JWT.create()
            .withSubject(authResult.getName())
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET_KEY));
        
        JwtResponse jwtResponse = new JwtResponse(token, authResult.getName());
        
        response.setContentType("application/json");
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        response.getWriter().write(new ObjectMapper().writeValueAsString(jwtResponse));
        response.getWriter().flush();
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, 
                                             HttpServletResponse response, 
                                             AuthenticationException failed) 
                                             throws IOException, ServletException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
            "{\"error\": \"" + failed.getMessage() + "\"}"
        );
        response.getWriter().flush();
    }
}