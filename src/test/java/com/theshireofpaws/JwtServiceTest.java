package com.theshireofpaws;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.theshireofpaws.security.JwtService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final long ONE_HOUR = 60 * 60 * 1000;

    @Test
    void createdTokenIsVerifiedWithTheSameSecret() {
        JwtService jwtService = new JwtService("test-secret", ONE_HOUR);

        String token = jwtService.createToken("admin@theshireofpaws.com");

        assertThat(jwtService.verifyAndGetSubject(token)).isEqualTo("admin@theshireofpaws.com");
    }

    @Test
    void tokenSignedWithAnotherSecretIsRejected() {
        String token = new JwtService("other-secret", ONE_HOUR).createToken("admin@theshireofpaws.com");

        assertThatThrownBy(() -> new JwtService("test-secret", ONE_HOUR).verifyAndGetSubject(token))
            .isInstanceOf(JWTVerificationException.class);
    }

    @Test
    void expiredTokenIsRejected() {
        JwtService jwtService = new JwtService("test-secret", -ONE_HOUR);

        String token = jwtService.createToken("admin@theshireofpaws.com");

        assertThatThrownBy(() -> jwtService.verifyAndGetSubject(token))
            .isInstanceOf(JWTVerificationException.class);
    }
}
