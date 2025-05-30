package com.chensoul.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void helloUnauthenticated() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void helloAuthenticated() throws Exception {
        mvc.perform(get("/hello")
                        .with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    public void helloAuthenticatingWithWrongUser() throws Exception {
        mvc.perform(get("/hello")
                        .with(httpBasic("bill", "password")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void helloAuthenticatingWithValidUser() throws Exception {
        mvc.perform(get("/hello")
                        .with(httpBasic("user", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void testCORSForTestEndpoint() throws Exception {
        mvc.perform(options("/hello")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "http://www.example.com")
                ).andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().string("Access-Control-Allow-Methods", "POST"))
                .andExpect(status().isOk());
    }

}
