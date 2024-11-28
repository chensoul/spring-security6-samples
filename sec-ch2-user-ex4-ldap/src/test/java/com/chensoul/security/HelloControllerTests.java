package com.chensoul.security;

import com.chensoul.security.config.SecurityConfig;
import com.chensoul.security.controller.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
@Import(SecurityConfig.class)
public class HelloControllerTests {

    @Autowired
    MockMvc mvc;

    @Test
    void rootWhenAuthenticatedThenSaysHelloUser() throws Exception {
        // @formatter:off
		this.mvc.perform(get("/hello")
			.with(httpBasic("user", "password")))
			.andExpect(content().string("Hello, user!"));
		// @formatter:on
    }

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        // @formatter:off
		this.mvc.perform(get("/hello"))
				.andExpect(status().isUnauthorized());
		// @formatter:on
    }

    @Test
    void tokenWhenBadCredentialsThen401() throws Exception {
        // @formatter:off
		this.mvc.perform(get("/hello")
				.with(httpBasic("user", "passwerd")))
				.andExpect(status().isUnauthorized());
		// @formatter:on
    }

}