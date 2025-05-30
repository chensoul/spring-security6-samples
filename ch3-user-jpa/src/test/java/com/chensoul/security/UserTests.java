package com.chensoul.security;

import com.chensoul.security.config.WithMockCustomUser;
import com.chensoul.security.config.WithUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void userWhenNotAuthenticated() throws Exception {
        // @formatter:off
		this.mockMvc.perform(get("/user"))
				.andExpect(status().isUnauthorized());
		// @formatter:on
    }

    @Test
    @WithUserDetails("user")
    void userWhenWithUserDetailsThenOk() throws Exception {
        // @formatter:off
		this.mockMvc.perform(get("/user"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(1)));
		// @formatter:on
    }

    @Test
    @WithUser
    void userWhenWithUserThenOk() throws Exception {
        // @formatter:off
		this.mockMvc.perform(get("/user"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(1)));
		// @formatter:on
    }

    @Test
    @WithMockCustomUser(username = "admin")
    void userWhenWithMockCustomUserThenOk() throws Exception {
        // @formatter:off
		this.mockMvc.perform(get("/user"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", equalTo("admin")));
		// @formatter:on
    }

    @Test
    @WithMockCustomUser(username = "admin")
    void userWhenWithMockCustomAdminThenOk() throws Exception {
        // @formatter:off
		this.mockMvc.perform(get("/user"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", equalTo("admin")));
		// @formatter:on
    }

}