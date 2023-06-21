package com.example.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "userauths")
public class UserAuth {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    private int token;

    public int getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setToken() {
        this.token = (int) (Math.random() * 1000000);
    }
}
