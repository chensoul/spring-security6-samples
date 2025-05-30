package com.chensoul.security.spring.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name);

        if (user == null) {
            throw new UsernameNotFoundException("user name not found");
        } else {
            return new org.springframework.security.core.userdetails.User(
                    user.getName(),
                    user.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        }
    }

}
