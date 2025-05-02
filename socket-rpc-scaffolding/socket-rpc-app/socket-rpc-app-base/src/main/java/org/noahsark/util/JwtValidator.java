package org.noahsark.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class JwtValidator {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private String secret;

    private int expirationDays;

    public JwtValidator() {
    }

    public JwtValidator(String secret, int expirationDays) {
        this.secret = secret;
        this.expirationDays = expirationDays;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getExpirationDays() {
        return expirationDays;
    }

    public void setExpirationDays(int expirationDays) {
        this.expirationDays = expirationDays;
    }

    public String getStringValue(String token, String key) {
        DecodedJWT jwt = JWT.decode(token);
        Claim claim = jwt.getClaim(key);
        return claim.asString();
    }

    public Long getLongValue(String token, String key) {
        DecodedJWT jwt = JWT.decode(token);
        Claim claim = jwt.getClaim(key);
        return claim.asLong();
    }

    public Integer getIntValue(String token, String key) {
        DecodedJWT jwt = JWT.decode(token);
        Claim claim = jwt.getClaim(key);
        return claim.asInt();
    }

    public boolean isExist(String token, String key) {
        DecodedJWT jwt = JWT.decode(token);
        Claim claim = jwt.getClaim(key);

        return claim != null;
    }

    public String getStringValue(DecodedJWT jwt, String key) {
        Claim claim = jwt.getClaim(key);
        return claim.asString();
    }

    public Long getLongValue(DecodedJWT jwt, String key) {
        Claim claim = jwt.getClaim(key);
        return claim.asLong();
    }

    public Integer getIntValue(DecodedJWT jwt, String key) {
        Claim claim = jwt.getClaim(key);
        return claim.asInt();
    }

    public boolean isExist(DecodedJWT jwt, String key) {
        Claim claim = jwt.getClaim(key);

        return claim != null;
    }

    public DecodedJWT verify(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        return verifier.verify(token);
    }

    public String createToken(Map<String, Object> payloadClaims) {
        return JWT.create()
                .withExpiresAt(getExpiresAt())
                .withPayload(payloadClaims)
                .sign(Algorithm.HMAC256(secret));
    }

    public Instant getExpiresAt() {
        return Instant.now().plus(expirationDays, ChronoUnit.DAYS);
    }
}
