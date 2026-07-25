package ru.java.device.service.petservice.config;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import ru.java.device.service.petservice.util.JwtParseUtil;

import java.time.Instant;
import java.util.*;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Profile("default")
    SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> {
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                                    "/actuator/**",
                                    "/swagger-ui/**",
                                    "/v3/api-docs/swagger-config",
                                    "/openapi.yml").permitAll()
                            .anyRequest().authenticated();
                }).oauth2ResourceServer(oauth2 -> {
                    oauth2.jwt(o -> o.jwtAuthenticationConverter(jwt -> {
                        return new JwtAuthenticationToken(jwt, JwtParseUtil.extractAuthorities(jwt));
                    }));
                }).build();
    }

    @Bean
    @Profile("dev")
    SecurityFilterChain clientSecurityFilterChainDev(HttpSecurity http) {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> {
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).authorizeHttpRequests(requests -> {
                    requests.anyRequest().permitAll();
                }).oauth2ResourceServer(oauth2 -> {
                    oauth2.jwt(o -> o.jwtAuthenticationConverter(jwt -> {
                        return new JwtAuthenticationToken(jwt, JwtParseUtil.extractAuthorities(jwt));
                    }));
                }).build();
    }

    @Bean
    @Profile("dev")
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
                log.info("error parse jwt token. Use valid jwt");
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
