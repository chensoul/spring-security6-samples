package com.chensoul.security.config;

import com.chensoul.security.spring.RedisUserCache;
import com.chensoul.security.spring.provider.jwt.JwtAuthenticationProvider;
import com.chensoul.security.spring.provider.jwt.JwtProperties;
import com.chensoul.security.spring.provider.jwt.JwtTokenAuthenticationProcessingFilter;
import com.chensoul.security.spring.provider.jwt.JwtTokenFactory;
import com.chensoul.security.spring.provider.jwt.RedisTokenCacheService;
import com.chensoul.security.spring.provider.jwt.RefreshTokenAuthenticationProvider;
import com.chensoul.security.spring.provider.jwt.RefreshTokenProcessingFilter;
import com.chensoul.security.spring.provider.jwt.SkipPathRequestMatcher;
import com.chensoul.security.spring.provider.rest.MfaProperties;
import com.chensoul.security.spring.provider.rest.RestAccessDeniedHandler;
import com.chensoul.security.spring.provider.rest.RestAuthenticationEntryPoint;
import com.chensoul.security.spring.provider.rest.RestAuthenticationFailureHandler;
import com.chensoul.security.spring.provider.rest.RestAuthenticationProvider;
import com.chensoul.security.spring.provider.rest.RestAuthenticationSuccessHandler;
import com.chensoul.security.spring.provider.rest.RestLoginProcessingFilter;
import com.chensoul.security.spring.user.CustomUserDetailsService;
import com.chensoul.security.spring.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableCaching
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties({JwtProperties.class, MfaProperties.class})
public class SecurityConfig {
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;
    private final MfaProperties mfaProperties;
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

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
        http.authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(jwtProperties.getPathsToSkip().toArray(new String[0])).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .headers(headers -> headers.cacheControl(withDefaults()).frameOptions(withDefaults()).disable())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(config -> config.accessDeniedHandler(new RestAccessDeniedHandler(objectMapper))
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint(objectMapper)))
                .addFilterBefore(restLoginProcessingFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtTokenAuthenticationProcessingFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(refreshTokenProcessingFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = new ArrayList<>();
        DaoAuthenticationProvider authenticationProvider = daoAuthenticationProvider();
        providers.add(authenticationProvider);
        providers.add(new RestAuthenticationProvider(userDetailsService(), passwordEncoder()));
        providers.add(new JwtAuthenticationProvider(jwtTokenFactory()));
        providers.add(new RefreshTokenAuthenticationProvider(userDetailsService(), jwtTokenFactory()));

        ProviderManager providerManager = new ProviderManager(providers);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenFactory jwtTokenFactory() {
        return new JwtTokenFactory(jwtProperties);
    }

    @Configuration
    @RequiredArgsConstructor
    @ConditionalOnBean(CacheManager.class)
    public class TokenCacheConfig {
        private final CacheManager cacheManager;

        @Bean
        @ConditionalOnMissingBean
        public RedisTokenCacheService tokenCacheService() {
            return new RedisTokenCacheService(cacheManager);
        }
    }

    protected RestLoginProcessingFilter restLoginProcessingFilter(AuthenticationManager authenticationManager) {
        RestLoginProcessingFilter filter = new RestLoginProcessingFilter(jwtProperties.getLoginUrl(),
                objectMapper,
                defaultAuthenticationSuccessHandler(),
                defaultAuthenticationFailureHandler());
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    protected JwtTokenAuthenticationProcessingFilter jwtTokenAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(jwtProperties.getPathsToSkip(), jwtProperties.getBaseUrl());
        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(defaultAuthenticationFailureHandler(), matcher);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    protected RefreshTokenProcessingFilter refreshTokenProcessingFilter(AuthenticationManager authenticationManager) {
        RefreshTokenProcessingFilter filter = new RefreshTokenProcessingFilter(jwtProperties.getTokenRefreshUrl(), objectMapper,
                defaultAuthenticationSuccessHandler(), defaultAuthenticationFailureHandler());
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public AuthenticationSuccessHandler defaultAuthenticationSuccessHandler() {
        return new RestAuthenticationSuccessHandler(jwtTokenFactory(), objectMapper, mfaProperties);
    }

    public AuthenticationFailureHandler defaultAuthenticationFailureHandler() {
        return new RestAuthenticationFailureHandler(objectMapper);
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setUserCache(new RedisUserCache(redisTemplate));
        return daoAuthenticationProvider;
    }

}
