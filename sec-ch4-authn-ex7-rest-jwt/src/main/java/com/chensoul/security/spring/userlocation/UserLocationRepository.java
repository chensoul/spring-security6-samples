package com.chensoul.security.spring.userlocation;

import com.chensoul.security.spring.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
    UserLocation findByCountryAndUser(String country, User user);
}