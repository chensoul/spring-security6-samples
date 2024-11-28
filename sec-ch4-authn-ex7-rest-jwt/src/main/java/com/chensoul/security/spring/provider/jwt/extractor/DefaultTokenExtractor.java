package com.chensoul.security.spring.provider.jwt.extractor;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;

public class DefaultTokenExtractor implements TokenExtractor {
    public static final String HEADER_PREFIX = "Bearer ";
    public static final String REQUEST_PREFIX = "accessToken";

    @Override
    public String extract(HttpServletRequest request) {
        String header = request.getHeader(JWT_TOKEN_HEADER_PARAM);
        if (StringUtils.isNotBlank(header)) {
            if (header.length() < HEADER_PREFIX.length()) {
                throw new AuthenticationServiceException("Invalid authorization header size.");
            }
            header = header.substring(HEADER_PREFIX.length(), header.length());
        } else {
            header = request.getParameter(REQUEST_PREFIX);
        }

        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }
        return header;
    }
}
