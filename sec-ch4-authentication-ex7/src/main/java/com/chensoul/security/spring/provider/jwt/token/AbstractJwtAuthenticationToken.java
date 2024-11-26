package com.chensoul.security.spring.provider.jwt.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class AbstractJwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -6212297506742428406L;

    private JwtToken jwtToken;
    private UserDetails userDetails;

    public AbstractJwtAuthenticationToken(JwtToken jwtToken) {
        super(null);
        this.jwtToken = jwtToken;
        this.setAuthenticated(false);
    }

    public AbstractJwtAuthenticationToken(UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.eraseCredentials();
        this.userDetails = userDetails;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userDetails;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.jwtToken = null;
    }
}
