package com.chensoul.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(ldapAuthenticationProvider());

        return http.build();
    }

    @Bean
    LdapAuthenticationProvider ldapAuthenticationProvider() {
        return new LdapAuthenticationProvider(authenticator());
    }

    @Bean
    BindAuthenticator authenticator() {
        FilterBasedLdapUserSearch search = new FilterBasedLdapUserSearch("ou=groups", "(uid={0})", contextSource());
        BindAuthenticator authenticator = new BindAuthenticator(contextSource());
        authenticator.setUserSearch(search);
        return authenticator;
    }

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        DefaultSpringSecurityContextSource dsCtx = new DefaultSpringSecurityContextSource("ldap://localhost:8389/dc=springframework,dc=org");
        dsCtx.setUserDn("uid={0},ou=people");
        return dsCtx;
    }

}
