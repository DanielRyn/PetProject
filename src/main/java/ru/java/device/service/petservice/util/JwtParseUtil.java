package ru.java.device.service.petservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.CollectionUtils;
import ru.java.device.service.petservice.model.JwtUserDto;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JwtParseUtil {

    @Value(value = "${spring.security.keycloak.account}")
    private static String KEYCLOAK_ACCOUNT;

    //TODO Это для аудита событий, потом будет использовать

    /**
     *
     * @return Текущий авторизованный пользователь
     */
    public static JwtUserDto getUser() {
        var claims = getClaims(((Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials()));
        return JwtUserDto.builder()
                .userId(claims.get("sub"))
                .userName(claims.get("preferred_username"))
                .userRoles(getUserRoles(claims))
                .clientName(claims.get("azp"))
                .build();
    }

    public static Map<String, Object> getClaims(Jwt jwt) {
        return jwt.getClaims();
    }

    public static List<Object> getUserRoles(Map<String, Object> claims) {
        try {
            return ((Map<String, Map<String, List<Object>>>) claims
                    .get("resource_access"))
                    .get(KEYCLOAK_ACCOUNT)
                    .get("roles");
        } catch (Exception e) {
            log.error("error parse jwt token", e);
            return Collections.emptyList();
        }
    }

    public static Collection<SimpleGrantedAuthority> extractAuthorities(Jwt jwt) {

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
