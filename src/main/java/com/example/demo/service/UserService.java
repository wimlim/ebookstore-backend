package com.example.demo.service;

public interface UserService {
    String login(String account, String password);
    String getUserProfile(Long token);
    String getAllUsers();

    String banUser(Long userId);

    String unbanUser(Long userId);
}
