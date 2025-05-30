package com.chensoul.security;

import com.chensoul.security.user.Role;
import com.chensoul.security.user.User;
import com.chensoul.security.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class})
public class MainTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testWithWrongUser() throws Exception {
        mvc.perform(formLogin())
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(username = "mary", password = "password")
    public void testWithMockUser() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, mary!")));
    }

    @Test
    public void testAuthenticationWithValidUser() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("mary");
        mockUser.setPassword(passwordEncoder.encode("password"));

        Role a = new Role();
        a.setName("read");
        a.setUser(mockUser);
        mockUser.setRoles(List.of(a));

        when(userRepository.findUserByUsername("mary"))
                .thenReturn(mockUser);

        mvc.perform(formLogin()
                        .user("mary")
                        .password("password"))
                .andExpect(authenticated());
    }

    @Test
    public void testAuthenticationWithInexistentUser() throws Exception {
        when(userRepository.findUserByUsername("mary"))
                .thenReturn(null);

        mvc.perform(formLogin()
                        .user("mary")
                        .password("password"))
                .andExpect(unauthenticated());
    }

    @Test
    public void testAuthenticationWithInvalidPassword() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("mary");
        mockUser.setPassword(passwordEncoder.encode("password"));

        Role a = new Role();
        a.setName("read");
        a.setUser(mockUser);
        mockUser.setRoles(List.of(a));

        when(userRepository.findUserByUsername("mary"))
                .thenReturn(mockUser);

        mvc.perform(formLogin()
                        .user("mary")
                        .password("pass1"))
                .andExpect(unauthenticated());
    }
}
