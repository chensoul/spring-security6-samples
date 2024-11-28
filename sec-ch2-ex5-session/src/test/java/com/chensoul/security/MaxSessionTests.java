package com.chensoul.security;

import com.chensoul.security.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockHttpSession;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringJUnitWebConfig(classes = SecurityConfig.class)
public class MaxSessionTests {

	@Test
	void run(WebApplicationContext context) throws Exception {
		// @formatter:off
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(springSecurity())
				.build();

		MvcResult mvcResult = mockMvc.perform(formLogin())
				.andExpect(authenticated())
				.andReturn();
		// @formatter:on

		MockHttpSession user1Session = (MockHttpSession) mvcResult.getRequest().getSession();

		// @formatter:off
		mockMvc.perform(get("/").session(user1Session))
				.andExpect(authenticated());
		// @formatter:on

		mockMvc.perform(formLogin()).andExpect(authenticated());

		// @formatter:off
		// session is terminated by user2
		mockMvc.perform(get("/").session(user1Session))
				.andExpect(unauthenticated());
		// @formatter:on
	}

}