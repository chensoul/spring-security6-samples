package com.chensoul.security.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationEvents {

    @EventListener
    public void onSuccess(AuthorizationGrantedEvent successEvent) {
        Authentication authentication = (Authentication) successEvent.getAuthentication().get();
        log.info("Authorization successful for the user {}", authentication.getName());
    }

    @EventListener
    public void onFailure(AuthorizationDeniedEvent deniedEvent) {
        Authentication authentication = (Authentication) deniedEvent.getAuthentication().get();

        log.error("Authorization failed for the user {} due to {}", authentication.getName(),
                deniedEvent.getAuthorizationDecision().toString());
    }

}