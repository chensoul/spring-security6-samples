package com.chensoul.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootApplication
public class Main {

    public void m() {
        UserDetails u = User.withUsername("bill")
                .password("password")
                .authorities("read", "write")
                .accountExpired(false)
                .disabled(true)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
