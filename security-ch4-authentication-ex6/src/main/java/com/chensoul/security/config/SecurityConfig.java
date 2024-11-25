package com.chensoul.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        var manager = new InMemoryUserDetailsManager();

        var user1 = User.withUsername("user")
                .password("pass")
                .authorities("READ")
                .build();

        var user2 = User.withUsername("jane")
                .password("pass")
                .authorities("WRITE")
                .build();

        manager.createUser(user1);
        manager.createUser(user2);

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();

        //http.authorizeRequests().anyRequest().hasAnyAuthority("WRITE", "READ");
        //http.authorizeRequests().anyRequest().hasAuthority("WRITE");
        http.authorizeRequests().anyRequest().access("hasAuthority('WRITE')");

//        String expression = "hasAuthority('read') and !hasAuthority('delete')";
//        http.authorizeRequests().anyRequest().access(expression);
//
//        http.authorizeRequests().anyRequest().hasRole("ADMIN");
//
//        http.authorizeRequests().anyRequest().access("T(java.time.LocalTime).now().isAfter(T(java.time.LocalTime).of(12, 0))");
    }
}
