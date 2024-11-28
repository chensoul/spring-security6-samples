package com.chensoul.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        User u = userRepository.findUserByUsername(username);
        if (u==null) {
            throw new UsernameNotFoundException("Problem during authentication!");
        }

        return new CustomUserDetails(u);
    }
}
