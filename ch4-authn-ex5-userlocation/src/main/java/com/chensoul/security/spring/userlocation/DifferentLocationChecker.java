package com.chensoul.security.spring.userlocation;

import com.chensoul.security.spring.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DifferentLocationChecker implements UserDetailsChecker {
    private final UserLocationService userLocationService;
    private final HttpServletRequest request;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void check(UserDetails userDetails) {
        String ip = SecurityUtils.getClientIP(request);
        NewLocationToken token = userLocationService.isNewLoginLocation(userDetails.getUsername(), ip);
        if (token != null) {
            String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

            eventPublisher.publishEvent(
                    new OnDifferentLocationLoginEvent(
                            request.getLocale(), userDetails.getUsername(), ip, token, appUrl));
            throw new UnusualLocationException("unusual location");
        }
    }
}