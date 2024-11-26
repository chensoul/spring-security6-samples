package com.chensoul.security.config;

import com.chensoul.security.service.CustomAuthenticationProvider;
import com.chensoul.security.service.JpaUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    final JpaUserDetailsService jpaUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin().defaultSuccessUrl("/main", true);
        http.authorizeRequests().anyRequest().authenticated();
        http.authenticationProvider(new CustomAuthenticationProvider(jpaUserDetailsService, passwordEncoder()));
        return http.build();
    }
}
