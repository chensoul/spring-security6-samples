package com.chensoul.security.spring.provider.jwt;

import com.chensoul.security.spring.provider.jwt.token.AccessJwtToken;
import com.chensoul.security.spring.provider.jwt.token.JwtToken;
import com.chensoul.security.spring.provider.rest.model.Authority;
import com.chensoul.security.spring.provider.rest.model.TokenPair;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFactory {
    private static final String SCOPES = "scopes";
    private static final String ENABLED = "enabled";
    private final JwtProperties jwtProperties;
    private TokenCacheService tokenCacheService = new DefaultTokenCacheService();

    public JwtToken createAccessJwtToken(UserDetails userDetails) {
        JwtBuilder jwtBuilder = setUpToken(userDetails, userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()), jwtProperties.getAccessTokenExpireTime());
        jwtBuilder.claim(ENABLED, userDetails.isEnabled());

        String token = jwtBuilder.compact();
        return new AccessJwtToken(token);
    }

    public UserDetails parseAccessJwtToken(String token) {
        Jws<Claims> jwsClaims = parseTokenClaims(token);
        Claims claims = jwsClaims.getBody();
        String subject = claims.getSubject();
        List<String> scopes = claims.get(SCOPES, List.class);
        if (scopes == null || scopes.isEmpty()) {
            throw new IllegalArgumentException("JWT Token doesn't have any scopes");
        }

        UserDetails userDetails = new User(subject, token, AuthorityUtils.createAuthorityList(scopes.toArray(new String[0])));

        if (tokenCacheService.isExpired(userDetails.getUsername(), claims.getIssuedAt().getTime())) {
            throw new JwtExpiredTokenException("Token has expired");
        }

        return userDetails;
    }

    public JwtToken createRefreshToken(UserDetails userDetails) {
        String token = setUpToken(userDetails, Collections.singletonList(Authority.REFRESH_TOKEN.name()), jwtProperties.getRefreshTokenExpireTime())
                .id(UUID.randomUUID().toString()).compact();

        return new AccessJwtToken(token);
    }

    public UserDetails parseRefreshToken(String token) {
        Jws<Claims> jwsClaims = parseTokenClaims(token);
        Claims claims = jwsClaims.getBody();
        String subject = claims.getSubject();
        @SuppressWarnings("unchecked")
        List<String> scopes = claims.get(SCOPES, List.class);
        if (scopes == null || scopes.isEmpty()) {
            throw new IllegalArgumentException("Refresh Token doesn't have any scopes");
        }
        if (!scopes.get(0).equals(Authority.REFRESH_TOKEN.name())) {
            throw new IllegalArgumentException("Invalid Refresh Token scope");
        }
        UserDetails userDetails = new User(subject, token, AuthorityUtils.createAuthorityList(scopes.toArray(new String[0])));

        if (tokenCacheService.isExpired(userDetails.getUsername(), claims.getIssuedAt().getTime())) {
            throw new JwtExpiredTokenException("Token has expired");
        }

        return userDetails;
    }

    public JwtToken createPreVerificationToken(UserDetails user, Long expirationTime) {
        JwtBuilder jwtBuilder = setUpToken(user, Collections.singletonList(Authority.PRE_VERIFICATION_TOKEN.name()), expirationTime);
        return new AccessJwtToken(jwtBuilder.compact());
    }

    private JwtBuilder setUpToken(UserDetails userDetails, List<String> scopes, long expirationTime) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername())
                .add(SCOPES, scopes)
                .build();

        ZonedDateTime currentTime = ZonedDateTime.now();

        return Jwts.builder()
                .setClaims(claims)
                .issuer(jwtProperties.getTokenIssuer())
                .issuedAt(Date.from(currentTime.toInstant()))
                .expiration(Date.from(currentTime.plusSeconds(expirationTime).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getTokenSigningKey());
    }

    public Jws<Claims> parseTokenClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtProperties.getTokenSigningKey()).build().parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("Token has Invalid", ex);
        } catch (SignatureException | ExpiredJwtException expiredEx) {
            throw new JwtExpiredTokenException(token, "Token has expired", expiredEx);
        }
    }

    public TokenPair createTokenPair(UserDetails userDetails) {
        JwtToken accessToken = createAccessJwtToken(userDetails);
        JwtToken refreshToken = createRefreshToken(userDetails);
        return new TokenPair(accessToken.token(), refreshToken.token(), userDetails.getAuthorities());
    }

}
