package ru.java.device.service.petservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.CollectionUtils;
import ru.java.device.service.petservice.util.JwtParseUtil;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain clientSecurityFilterChainProd(HttpSecurity http) {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> {
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/v3/api-docs/swagger-config",
                                    "/openapi.yml").permitAll()
                            .anyRequest().authenticated();
                }).oauth2ResourceServer(oauth2 -> {
                    oauth2.jwt(o -> o.jwtAuthenticationConverter(jwt -> new JwtAuthenticationToken(jwt, extractAuthorities(jwt))));
                }).build();
    }

    private Collection<SimpleGrantedAuthority> extractAuthorities(Jwt jwt) {
        Map<String, Object> claims = JwtParseUtil.getClaims(jwt);
        List<Object> roles = JwtParseUtil.getUserRoles(claims);

        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
