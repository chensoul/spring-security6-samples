package com.chensoul.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

class CompromisedPasswordAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler("/");

    private final CompromisedPasswordChecker compromisedPasswordChecker = new HaveIBeenPwnedRestApiPasswordChecker();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CompromisedPasswordDecision decision = this.compromisedPasswordChecker
                .check((String) authentication.getCredentials());
        if (decision.isCompromised()) {
            HttpSession session = request.getSession(false);
            session.setAttribute("compromised_password", true);
        }
        this.successHandler.onAuthenticationSuccess(request, response, authentication);
    }

}