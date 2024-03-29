package com.example.demo.entity;

import javax.persistence.*;
import java.util.List;

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
    private boolean is_admin;
    private boolean is_banned;
    private String email;

    @Lob
    private byte[] avatar;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserAuth userAuth;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

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

    public boolean isAdmin() {
        return is_admin;
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

    public UserAuth getUserAuth() {
        return userAuth;
    }
    public boolean getIsBanned() {
        return is_banned;
    }
    public void setIsBanned(boolean is_banned) {
        this.is_banned = is_banned;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsAdmin(boolean b) {
        this.is_admin = b;
    }
    public byte[] getAvatar() {
        return avatar;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
