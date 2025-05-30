package com.chensoul.security.spring.provider.jwt;

import com.chensoul.security.spring.provider.jwt.token.JwtToken;
import com.chensoul.security.spring.provider.jwt.token.RefreshAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final JwtTokenFactory jwtTokenFactory;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");
        JwtToken jwtToken = (JwtToken) authentication.getCredentials();
        UserDetails unsafeUser = jwtTokenFactory.parseRefreshToken(jwtToken.token());
        UserDetails userDetails = authenticateByUserId(unsafeUser.getUsername());

        return new RefreshAuthenticationToken(userDetails);
    }

    private UserDetails authenticateByUserId(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found by refresh token");
        }

        return user;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (RefreshAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
