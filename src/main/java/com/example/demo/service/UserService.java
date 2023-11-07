package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
    String login(String account, String password);
    String getUserProfile(Long token);
    String getAllUsers();

    String banUser(Long userId);

    String unbanUser(Long userId);

    String register(String username, String password, String email);

    byte[] getUserAvatar(Long token);

    String updateUserProfile(Long token, String firstname, String lastname, String twitter, String notes);

    String updateUserAvatar(Long token, byte[] avatar);
}
