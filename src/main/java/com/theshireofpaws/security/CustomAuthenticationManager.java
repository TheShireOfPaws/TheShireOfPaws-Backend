package com.theshireofpaws.security;

import com.theshireofpaws.service.interfaces.AdminUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {
    
    private final AdminUserService adminUserService;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public CustomAuthenticationManager(AdminUserService adminUserService, 
                                      BCryptPasswordEncoder passwordEncoder) {
        this.adminUserService = adminUserService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = adminUserService.loadUserByUsername(authentication.getName());
        
        if (!passwordEncoder.matches(authentication.getCredentials().toString(), 
                                     userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        
        return new UsernamePasswordAuthenticationToken(
            authentication.getName(), 
            userDetails.getPassword(), 
            userDetails.getAuthorities()
        );
    }
}