package com.chensoul.security.spring.provider.jwt;

public interface TokenCacheService {
    String USERS_SESSION_INVALIDATION_CACHE = "userSessionsInvalidation";

    boolean isExpired(String username, long issueTime);

}
