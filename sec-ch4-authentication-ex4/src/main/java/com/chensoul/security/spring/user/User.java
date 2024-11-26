package com.chensoul.security.spring.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
