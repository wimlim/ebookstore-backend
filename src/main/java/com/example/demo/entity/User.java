package com.example.demo.entity;
import com.alibaba.fastjson.JSONArray;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;

    private String lastname;

    private String twitter;

    private String notes;
    private String account;
    private String password;

    public Long getId() {
        return id;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstname;
    }
    public String getLastName() {
        return lastname;
    }
    public String getTwitter() {
        return twitter;
    }
    public String getNotes() {
        return notes;
    }

}
