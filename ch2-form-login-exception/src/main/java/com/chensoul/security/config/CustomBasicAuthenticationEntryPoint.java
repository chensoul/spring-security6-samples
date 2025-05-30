package com.chensoul.security.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        String message = (e != null && e.getMessage() != null) ? e.getMessage() : "Unauthorized";

        log.error(message, e);

        httpServletResponse.addHeader("message", "Luke, I am your father!");
        httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value());
    }
}
