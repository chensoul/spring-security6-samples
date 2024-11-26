package com.chensoul.security.config;

import com.chensoul.security.spring.user.User;
import com.chensoul.security.spring.user.UserRepository;
import com.chensoul.security.spring.userlocation.UserLocation;
import com.chensoul.security.spring.userlocation.UserLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class Initializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserLocationRepository userLocationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Running Initializer.....");

        var admin = new User("admin", "ADMIN", passwordEncoder.encode("password"));
        var user = new User("user", "USER", passwordEncoder.encode("password"));

        userRepository.save(admin);
        userRepository.save(user);

        userLocationRepository.save(new UserLocation("局域网", user));
    }
}
