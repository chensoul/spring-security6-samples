package com.chensoul.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@SpringBootTest
@AutoConfigureWebTestClient
class EndpointTests {

    @Autowired
    private WebTestClient client;

    @Test
    void testCallHelloWithoutUser() {
        client.get()
                .uri("/hello")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testCallHelloWithUser() {
        client.mutateWith(mockJwt())
                .get()
                .uri("/hello")
                .exchange()
                .expectStatus().isOk();
    }
}
