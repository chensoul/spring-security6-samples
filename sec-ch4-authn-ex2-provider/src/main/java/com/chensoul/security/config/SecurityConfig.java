package com.chensoul.security.config;

import com.chensoul.security.provider.CustomUsernamePasswordAuthenticationProvider;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
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
        http.authorizeRequests(auth -> auth.anyRequest().authenticated())
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()) // Only HTTP
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .authenticationProvider(customUsernamePasswordAuthenticationProvider()); //这是增加一个
        return http.build();
    }

//    //方式 2
//    @Bean
//    public SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {
//        http.authorizeRequests(auth -> auth.anyRequest().authenticated())
//                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()) // Only HTTP
//                .httpBasic(withDefaults())
//                .formLogin(withDefaults());
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
