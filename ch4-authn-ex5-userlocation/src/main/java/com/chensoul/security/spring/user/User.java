package com.chensoul.security.spring.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String role;

    private String password;

    public User() {
    }

    public User(String name, String role, String password) {
        this.name = name;
        this.role = role;
        this.password = password;
    }
}
