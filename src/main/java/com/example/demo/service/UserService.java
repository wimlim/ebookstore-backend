package com.example.demo.service;

public interface UserService {
    String login(String account, String password);
    String getUserProfile(Long token);
}