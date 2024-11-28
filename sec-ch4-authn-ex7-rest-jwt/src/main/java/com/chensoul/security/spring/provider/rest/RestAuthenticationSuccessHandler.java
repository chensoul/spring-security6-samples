package com.chensoul.security.spring.provider.rest;

import com.chensoul.security.spring.provider.jwt.JwtTokenFactory;
import com.chensoul.security.spring.provider.rest.model.Authority;
import com.chensoul.security.spring.provider.rest.model.TokenPair;
import com.chensoul.security.spring.provider.rest.token.TwoFaAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenFactory tokenFactory;
    private final ObjectMapper objectMapper;
    private final MfaProperties mfaProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        TokenPair tokenPair;
        if (authentication instanceof TwoFaAuthenticationToken) {
            long preVerificationTokenLifetime = mfaProperties.getTotalAllowedTimeForVerification();
            tokenPair = new TokenPair();
            tokenPair.setAccessToken(tokenFactory.createPreVerificationToken(userDetails, preVerificationTokenLifetime).token());
            tokenPair.setAuthorities(AuthorityUtils.createAuthorityList(Authority.PRE_VERIFICATION_TOKEN.name()));
        } else {
            tokenPair = tokenFactory.createTokenPair(userDetails);
        }

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), tokenPair);

        clearAuthenticationAttributes(request);
    }

    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session==null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
