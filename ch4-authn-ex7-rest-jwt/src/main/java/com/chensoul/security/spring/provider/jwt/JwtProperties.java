package com.chensoul.security.spring.provider.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ConfigurationProperties(prefix = "security.jwt", ignoreUnknownFields = false)
public class JwtProperties {
    public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";
    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";

    private boolean enabled = true;
    private String baseUrl = TOKEN_BASED_AUTH_ENTRY_POINT;
    private String loginUrl = FORM_BASED_LOGIN_ENTRY_POINT;
    private String tokenRefreshUrl = TOKEN_REFRESH_ENTRY_POINT;

    private List<String> pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENTRY_POINT, FORM_BASED_LOGIN_ENTRY_POINT, "/login", "/api/noauth/**", "/error", "/actuator/**", "/api/system/mail/oauth2/code");
    private Integer accessTokenExpireTime = 9000;
    private Integer refreshTokenExpireTime = 604800;
    private String tokenIssuer = "chensoul.com";
    private String tokenSigningKey = "secret12345678901234567890123456789012345678901234567890123456789012345678901234567890";
}
