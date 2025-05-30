package com.chensoul.security.user;

import com.chensoul.security.config.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/user")
    public User user(@CurrentUser User user) {
        return user;
    }

}