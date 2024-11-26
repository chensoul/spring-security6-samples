package com.chensoul.security.spring.userlocation;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DifferentLocationLoginListener implements ApplicationListener<OnDifferentLocationLoginEvent> {
    private final MessageSource messages;

    @Override
    public void onApplicationEvent(final OnDifferentLocationLoginEvent event) {
        final String enableLocUri = event.getAppUrl() + "/user/enableNewLoc?token=" + event.getToken()
                .getToken();
        final String changePassUri = event.getAppUrl() + "/changePassword.html";
        final String subject = "Login attempt from different location";
        final String message = messages.getMessage("message.differentLocation", new Object[]{new Date().toString(), event.getToken()
                .getUserLocation()
                .getCountry(), event.getIp(), enableLocUri, changePassUri}, event.getLocale());

        log.error("{}: {}", subject, message);
    }

}
