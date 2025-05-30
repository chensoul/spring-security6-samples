package com.chensoul.security.spring.userlocation;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table
@Entity
public class NewLocationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = UserLocation.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_location_id")
    private UserLocation userLocation;

    public NewLocationToken(String token, UserLocation userLocation) {
        this.token = token;
        this.userLocation = userLocation;
    }

    public NewLocationToken() {

    }
}