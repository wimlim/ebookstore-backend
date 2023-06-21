package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> payload) {
        String account = payload.get("account");
        String password = payload.get("password");
        return userService.login(account, password);
    }

    @GetMapping("/profile/{token}")
    public String getUserProfile(@PathVariable("token") Long token) {
        return userService.getUserProfile(token);
    }

    @GetMapping("/all")
    public String getAllUsers() {
        return userService.getAllUsers();
    }
    @PutMapping("/ban/{id}")
    public String banUser(@PathVariable("id") Long userId) {
        return userService.banUser(userId);
    }
    @PutMapping("/unban/{id}")
    public String unbanUser(@PathVariable("id") Long userId) {
        return userService.unbanUser(userId);
    }
}
