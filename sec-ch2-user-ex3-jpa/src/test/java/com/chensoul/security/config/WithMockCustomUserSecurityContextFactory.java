package com.chensoul.security.config;

import com.chensoul.security.user.JpaUserDetailsService;
import com.chensoul.security.user.Role;
import com.chensoul.security.user.User;
import com.chensoul.security.user.UserRepository;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser mockCustomUser) {
        String username = mockCustomUser.username();
        // a stub CustomUserRepository that returns the user defined in the annotation
        UserRepository userRepository = (name) -> new User(mockCustomUser.id(), username, "", List.of(new Role(1, "USER")));
        JpaUserDetailsService userDetailsService = new JpaUserDetailsService(userRepository);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities()));
        return securityContext;
    }

}