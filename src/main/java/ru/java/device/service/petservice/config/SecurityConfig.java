package ru.java.device.service.petservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import ru.java.device.service.petservice.util.JwtParseUtil;

import java.util.*;

@Slf4j
@Configuration
@EnableWebSecurity
@Profile(value = "default")
public class SecurityConfig {

    @Bean
    SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> {
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers("/actuator/**").permitAll()
                            .anyRequest().authenticated()
                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/v3/api-docs/swagger-config",
                                    "/openapi.yml").permitAll();
                }).oauth2ResourceServer(oauth2 -> {
                    oauth2.jwt(o -> o.jwtAuthenticationConverter(jwt -> {
                        return new JwtAuthenticationToken(jwt, JwtParseUtil.extractAuthorities(jwt));
                    }));
                }).build();
    }
}
