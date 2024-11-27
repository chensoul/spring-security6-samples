package com.chensoul.security.spring.provider.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPair {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private Collection<? extends GrantedAuthority> authorities;
}
