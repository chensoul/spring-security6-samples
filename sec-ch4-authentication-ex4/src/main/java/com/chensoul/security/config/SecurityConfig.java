package com.chensoul.security.config;

import com.chensoul.security.spring.RedisUserCache;
import com.chensoul.security.spring.user.CustomUserDetailsService;
import com.chensoul.security.spring.user.UserRepository;
import com.chensoul.security.spring.userlocation.DifferentLocationChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final UserRepository userRepository;
    private final DifferentLocationChecker differentLocationChecker;
    private final RedisTemplate redisTemplate;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(daoAuthenticationProvider());
        http.authorizeRequests().anyRequest().authenticated();
        http.httpBasic();
        http.formLogin();
        return http.build();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPostAuthenticationChecks(differentLocationChecker);
        daoAuthenticationProvider.setUserCache(new RedisUserCache(redisTemplate));

        //security 6.4
//        daoAuthenticationProvider.setCompromisedPasswordChecker(new ResourcePasswordChecker(
//                new ClassPathResource("10-million-password-list-top-1000000.txt")));
        return daoAuthenticationProvider;
    }

}
