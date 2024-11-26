package com.chensoul.security.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY;
import static org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices.TWO_WEEKS_S;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(requests -> requests
                .anyRequest().authenticated());
        http.formLogin(form -> {
            form.defaultSuccessUrl("/about")
                    .loginPage("/login")
                    .loginProcessingUrl("/doLogin").permitAll();
        });
        http.httpBasic();
        http.csrf().disable();

        http.rememberMe(rememberMe -> {
            rememberMe.key(SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY)
                    .tokenValiditySeconds(TWO_WEEKS_S)
                    .rememberMeParameter("remember") //default is remember-me
                    .tokenRepository(persistentTokenRepository())
            ;
        });
        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsService = new InMemoryUserDetailsManager();

        var user = org.springframework.security.core.userdetails.User.withUsername("user")
                .password("pass")
                .authorities("read")
                .build();

        userDetailsService.createUser(user);
        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
