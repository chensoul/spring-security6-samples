package com.chensoul.security.config;

import com.chensoul.security.spring.CustomAccessDeniedHandler;
import com.chensoul.security.spring.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(auth -> auth.anyRequest().authenticated())
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()) // Only HTTP
                .formLogin(withDefaults())
                .httpBasic(basic -> {
                    basic.realmName("OTHER");
                    basic.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint());
                })
                .exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }
}
