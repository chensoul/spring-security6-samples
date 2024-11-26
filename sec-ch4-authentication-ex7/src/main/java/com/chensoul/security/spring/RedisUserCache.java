package com.chensoul.security.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@RequiredArgsConstructor
public class RedisUserCache implements UserCache {
    private final RedisTemplate redisTemplate;

    @Override
    public UserDetails getUserFromCache(String username) {
        return (UserDetails) redisTemplate.opsForValue().get(username);
    }

    @Override
    public void putUserInCache(UserDetails user) {
        redisTemplate.opsForValue().set(user.getUsername(), user);
    }

    @Override
    public void removeUserFromCache(String username) {
        redisTemplate.delete(username);
    }
}
