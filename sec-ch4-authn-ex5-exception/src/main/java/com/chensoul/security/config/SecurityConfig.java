package com.chensoul.security.config;

import com.chensoul.security.spring.CustomAccessDeniedHandler;
import com.chensoul.security.spring.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()); // Only HTTP
        http.httpBasic(basic -> {
            basic.realmName("OTHER");
            basic.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint());
        });
        http.formLogin();

        http.authorizeRequests().anyRequest().authenticated();

        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }
}
