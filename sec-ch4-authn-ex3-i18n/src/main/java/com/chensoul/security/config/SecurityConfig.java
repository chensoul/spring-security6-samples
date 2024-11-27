package com.chensoul.security.config;

import com.chensoul.security.provider.CustomUsernamePasswordAuthenticationProvider;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()); // Only HTTP
        http.httpBasic();
        http.formLogin();

        http.authorizeRequests().anyRequest().authenticated();
        http.authenticationProvider(customUsernamePasswordAuthenticationProvider());
        return http.build();
    }

    //    @Bean
    public CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider() {
        return new CustomUsernamePasswordAuthenticationProvider(userDetailsService(), passwordEncoder());
    }

//    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
//                                                       PasswordEncoder passwordEncoder) {
//        CustomUsernamePasswordAuthenticationProvider authenticationProvider =
//                new CustomUsernamePasswordAuthenticationProvider(userDetailsService, passwordEncoder);
//        ProviderManager providerManager = new ProviderManager(authenticationProvider);
//        providerManager.setEraseCredentialsAfterAuthentication(false);
//        return providerManager;
//    }
}
