package com.theshireofpaws.security;

import com.theshireofpaws.security.filter.JWTAuthenticationFilter;
import com.theshireofpaws.security.filter.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";

    private final CustomAuthenticationManager authenticationManager;

    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    public SecurityConfig(CustomAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**", "/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/files/upload").hasRole(ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/api/files").hasRole(ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/dogs/**").permitAll()
                .requestMatchers("/api/dogs/**").hasRole(ADMIN)
                .requestMatchers(HttpMethod.POST, "/api/adoption-requests").permitAll()
                .requestMatchers("/api/adoption-requests/**").hasRole(ADMIN)
                .anyRequest().authenticated()
            )
            .addFilter(new JWTAuthenticationFilter(authenticationManager))
            .addFilterAfter(new JWTAuthorizationFilter(), JWTAuthenticationFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
