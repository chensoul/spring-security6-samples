package com.chensoul.security.spring.provider.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;

public class RestAuthenticationDetailSource implements
        AuthenticationDetailsSource<HttpServletRequest, RestAuthenticationDetail> {

    @Override
    public RestAuthenticationDetail buildDetails(HttpServletRequest context) {
        return new RestAuthenticationDetail(context);
    }
}
