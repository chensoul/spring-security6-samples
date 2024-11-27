package com.chensoul.security.spring.provider.jwt.token;

import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends AbstractJwtAuthenticationToken {

    private static final long serialVersionUID = -8487219769037942225L;

    public JwtAuthenticationToken(JwtToken jwtToken) {
        super(jwtToken);
    }

    public JwtAuthenticationToken(UserDetails userDetails) {
        super(userDetails);
    }
}
