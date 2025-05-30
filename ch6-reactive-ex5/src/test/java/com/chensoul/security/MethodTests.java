package com.chensoul.security;

import com.chensoul.security.controller.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.test.StepVerifier;

@SpringBootTest
class MethodTests {

    @Autowired
    private HelloController helloController;

    @Test
    void testCallHelloWithoutUser() {
        StepVerifier.create(helloController.hello())
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @WithMockUser
    void testCallHelloWithUserButWrongRole() {
        StepVerifier.create(helloController.hello())
                .expectError(AccessDeniedException.class)
                .verify();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCallHelloWithUserAndAdminRole() {
        StepVerifier.create(helloController.hello())
                .expectNext("Hello!")
                .verifyComplete();
    }
}
