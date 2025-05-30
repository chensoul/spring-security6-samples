package com.chensoul.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testFailedAuthentication() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(unauthenticated());
    }

    @Test
    @WithUserDetails("user")
    public void testSuccessfulAuthentication() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("jane")
    public void testSuccessfulAuthorization() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }
}
