package com.chensoul.security.config;

import com.chensoul.security.provider.CustomUsernamePasswordAuthenticationProvider;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    final DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    public CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider() {
        return new CustomUsernamePasswordAuthenticationProvider(userDetailsService(), passwordEncoder());
    }

    // 方式 1
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()); // Only HTTP

        http.httpBasic();
        http.formLogin();

        http.authorizeRequests().anyRequest().authenticated();

        //这是增加一个
        http.authenticationProvider(customUsernamePasswordAuthenticationProvider());
        return http.build();
    }

//    //方式 2
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()); // Only HTTP
//
//        http.httpBasic();
//        http.formLogin();
//
//        http.authorizeRequests().anyRequest().authenticated();
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager() {
//        CustomUsernamePasswordAuthenticationProvider authenticationProvider = customUsernamePasswordAuthenticationProvider();
//        ProviderManager providerManager = new ProviderManager(authenticationProvider);
//        providerManager.setEraseCredentialsAfterAuthentication(false);
//        return providerManager;
//    }
}
