package com.chensoul.security;

import com.chensoul.security.user.User;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserITests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void login() {
        User result = this.rest.withBasicAuth("user", "password")
                .getForObject("/user", User.class);
        assertThat(result.getUsername()).isEqualTo("user");
    }

}