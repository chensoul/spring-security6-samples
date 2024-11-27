package com.chensoul.security.spring.provider.jwt.token;

public class AccessJwtToken implements JwtToken {
    private final String token;

    public AccessJwtToken(String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return "";
    }
}
