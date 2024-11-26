package com.chensoul.security.spring.provider.rest.token;

import com.chensoul.security.spring.provider.jwt.token.AbstractJwtAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class TwoFaAuthenticationToken extends AbstractJwtAuthenticationToken {
    public TwoFaAuthenticationToken(UserDetails userDetails) {
        super(userDetails);
    }
}
