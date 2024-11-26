package com.chensoul.security.spring.provider.jwt;

import java.util.Optional;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class RedisTokenCacheService implements TokenCacheService {
    private final CacheManager cacheManager;

    @EventListener(classes = UserAuthDataChangedEvent.class)
    public void onUserAuthDataChanged(UserAuthDataChangedEvent event) {
        if (event.getId()!=null) {
            log.info("User [{}] auth data has changed, set jwt token expired time to {}", event.getId(), event.getTs());

            cacheManager.getCache(USERS_SESSION_INVALIDATION_CACHE).put(event.getId().toString(), event.getTs());
        }
    }

    @Override
    public boolean isExpired(String username, long issueTime) {
        return isTokenExpired(username, issueTime);
    }

    private Boolean isTokenExpired(String username, long issueTime) {
        return Optional.ofNullable(cacheManager.getCache(USERS_SESSION_INVALIDATION_CACHE)
                .get(username)).map(op -> isTokenExpired(issueTime, (Long) op.get())).orElse(false);
    }

    private boolean isTokenExpired(long issueTime, Long expiredTime) {
        return MILLISECONDS.toSeconds(issueTime) < MILLISECONDS.toSeconds(expiredTime);
    }
}
