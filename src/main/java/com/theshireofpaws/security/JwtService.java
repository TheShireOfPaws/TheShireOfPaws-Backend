package com.theshireofpaws.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtService {

    static final String DEV_SECRET = "dev-only-secret-change-me";

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expirationMillis;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long expirationMillis) {
        if (DEV_SECRET.equals(secret)) {
            log.warn("JWT_SECRET is not set: using the development secret. Never do this in production.");
        }
        this.algorithm = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(algorithm).build();
        this.expirationMillis = expirationMillis;
    }

    public String createToken(String subject) {
        return JWT.create()
            .withSubject(subject)
            .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis))
            .sign(algorithm);
    }

    public String verifyAndGetSubject(String token) throws JWTVerificationException {
        return verifier.verify(token).getSubject();
    }
}
