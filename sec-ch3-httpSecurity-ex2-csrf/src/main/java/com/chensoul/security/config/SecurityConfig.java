package com.chensoul.security.config;

import com.chensoul.security.csrf.CustomCsrfTokenRepository;
import com.chensoul.security.csrf.JpaTokenRepository;
import com.chensoul.security.filter.CsrfTokenLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final JpaTokenRepository jpaTokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(requests -> requests.anyRequest().authenticated());
        http.formLogin(form -> {
            form.defaultSuccessUrl("/about")
                    .loginPage("/login")
                    .loginProcessingUrl("/doLogin").permitAll();
        });
        http.httpBasic();

        http.addFilterAfter(new CsrfTokenLogger(), CsrfFilter.class);

//        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http.csrf(c -> {
            c.csrfTokenRepository(customTokenRepository())
//                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                    .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)

            ;
        });
        return http.build();
    }

    @Bean
    public CsrfTokenRepository customTokenRepository() {
        return new CustomCsrfTokenRepository(jpaTokenRepository);
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
