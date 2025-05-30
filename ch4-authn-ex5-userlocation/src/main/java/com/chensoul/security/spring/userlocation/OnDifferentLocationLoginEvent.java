package com.chensoul.security.spring.userlocation;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
public class OnDifferentLocationLoginEvent extends ApplicationEvent {
    private final Locale locale;
    private final String username;
    private final String ip;
    private final NewLocationToken token;
    private final String appUrl;

    public OnDifferentLocationLoginEvent(Locale locale, String username, String ip, NewLocationToken token, String appUrl) {
        super(token);
        this.locale = locale;
        this.username = username;
        this.ip = ip;
        this.token = token;
        this.appUrl = appUrl;
    }

}
