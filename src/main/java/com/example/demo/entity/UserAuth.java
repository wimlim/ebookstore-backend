package com.example.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "userauths")
public class UserAuth {
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    private String token;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
