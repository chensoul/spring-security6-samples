package com.chensoul.security.spring.userlocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewLocationTokenRepository extends JpaRepository<NewLocationToken, Long> {

    NewLocationToken findByToken(String token);

    NewLocationToken findByUserLocation(UserLocation userLocation);

}
