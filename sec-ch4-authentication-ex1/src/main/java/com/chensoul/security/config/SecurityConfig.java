package com.chensoul.security.config;

import com.chensoul.security.filter.AuthenticationLoggingFilter;
import com.chensoul.security.filter.RequestValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(
                        new RequestValidationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(
                        new AuthenticationLoggingFilter(), BasicAuthenticationFilter.class)
                .authorizeRequests().anyRequest().permitAll();

        return http.build();
    }
}
