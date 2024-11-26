package com.chensoul.security.spring.provider.jwt;

import com.chensoul.security.spring.provider.jwt.token.JwtAuthenticationToken;
import com.chensoul.security.spring.provider.jwt.token.JwtToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenFactory tokenFactory;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtToken accessToken = (JwtToken) authentication.getCredentials();
        UserDetails userDetails = authenticate(accessToken.token());
        return new JwtAuthenticationToken(userDetails);
    }

    public UserDetails authenticate(String accessToken) throws AuthenticationException {
        if (StringUtils.isEmpty(accessToken)) {
            throw new BadCredentialsException("Token is invalid");
        }
        return tokenFactory.parseAccessJwtToken(accessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
