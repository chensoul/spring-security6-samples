package com.chensoul.security.spring.provider.jwt;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
public class DefaultTokenCacheService implements TokenCacheService {
    @Override
    public boolean isExpired(String username, long issueTime) {
        return false;
    }
}
