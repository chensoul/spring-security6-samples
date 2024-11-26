package com.chensoul.security.spring.provider.jwt.token;


import org.springframework.security.core.userdetails.UserDetails;

public class RefreshAuthenticationToken extends AbstractJwtAuthenticationToken {

    private static final long serialVersionUID = -1311042791508924523L;

    public RefreshAuthenticationToken(JwtToken jwtToken) {
        super(jwtToken);
    }

    public RefreshAuthenticationToken(UserDetails userDetails) {
        super(userDetails);
    }
}
