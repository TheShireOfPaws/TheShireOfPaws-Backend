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

import java.util.Arrays;

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

                .requestMatchers("/h2-console/**").permitAll()
                
                .requestMatchers("/api/auth/**").permitAll()
                
                .requestMatchers(HttpMethod.GET, "/api/files/download/**").permitAll()     // ⭐ Ver imágenes (público)
                .requestMatchers(HttpMethod.POST, "/api/files/upload").hasRole("ADMIN")    // Subir (admin)
                .requestMatchers(HttpMethod.DELETE, "/api/files/**").hasRole("ADMIN")      // Eliminar (admin)
                
               
                .requestMatchers(HttpMethod.GET, "/api/dogs/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/dogs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/dogs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/dogs/**").hasRole("ADMIN")
                
               
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
        
     
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "http://localhost:5174",
            "http://localhost:3000"
        ));
        
     
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
      
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
      
        configuration.setAllowCredentials(true);
        
       
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}