package ru.java.device.service.petservice.config;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.*;

@Slf4j
@Configuration
@EnableWebSecurity
@Profile("dev")
public class JwtDecoderConfigDev {

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            try {
                SignedJWT signedJWT = (SignedJWT) JWTParser.parse(token);

                Map<String, Object> claims = signedJWT.getJWTClaimsSet().getClaims();
                return Jwt.withTokenValue(token)
                        .headers(h -> h.putAll(signedJWT.getHeader().toJSONObject()))
                        .claims(c -> c.putAll(claims))
                        .issuedAt(toInstant(signedJWT.getJWTClaimsSet().getIssueTime()))
                        .expiresAt(toInstant(signedJWT.getJWTClaimsSet().getExpirationTime()))
                        .build();
            } catch (Exception e) {
                log.error("error parse jwt token. Use daefault jwt", e);
                return Jwt.withTokenValue(token)
                        .claim("", "")
                        .header("", "").build();
            }
        };
    }

    private Instant toInstant(Date date) {
        return date != null ? date.toInstant() : null;
    }
}
