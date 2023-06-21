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
    @PostMapping("/register") // 修改此处的路径为/register
    public String register(@RequestBody Map<String, String> payload) { // 修改方法名为register
        String username = payload.get("username"); // 修改获取用户名的键为"username"
        String password = payload.get("password"); // 修改获取密码的键为"password"
        String email = payload.get("email"); // 修改获取邮箱的键为"email"
        return userService.register(username, password, email); // 调用注册的方法
    }
}
