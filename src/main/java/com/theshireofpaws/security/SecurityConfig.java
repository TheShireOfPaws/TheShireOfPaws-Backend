package com.theshireofpaws.security;

import com.theshireofpaws.security.filter.JWTAuthenticationFilter;
import com.theshireofpaws.security.filter.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    
    private final CustomAuthenticationManager authenticationManager;
    
    public SecurityConfig(CustomAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(authenticationManager);
        
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                
                // Dogs - Public GET, Admin-only POST/PUT/DELETE
                .requestMatchers(HttpMethod.GET, "/api/dogs/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/dogs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/dogs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/dogs/**").hasRole("ADMIN")
                
                // Adoption Requests - Public POST (submit), Admin-only GET/PUT
                .requestMatchers(HttpMethod.POST, "/api/adoption-requests").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/adoption-requests/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/adoption-requests/**").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .addFilter(authenticationFilter)
            .addFilterAfter(new JWTAuthorizationFilter(), JWTAuthenticationFilter.class)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}