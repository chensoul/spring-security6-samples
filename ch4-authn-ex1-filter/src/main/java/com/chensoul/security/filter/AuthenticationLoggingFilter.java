package com.chensoul.security.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Slf4j
public class AuthenticationLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        String requestId = httpRequest.getHeader("Request-Id");
        log.info("Successfully authenticated request with id {}", requestId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
            log.info("User {} is successfully authenticated and has the authorities {}", authentication.getName(), authentication.getAuthorities().toString());
        }
        filterChain.doFilter(request, response);
    }
}
