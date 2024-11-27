package com.chensoul.security.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @ResponseBody
    @GetMapping("/hello")
    public String hello(Authentication authentication) {
        return "Hello, " + getName(authentication) + "!";
    }

    private static String getName(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return authentication.getName();
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            return ((OAuth2AuthenticationToken) authentication).getPrincipal().getName();

        }
        return "unknown";
    }
}
