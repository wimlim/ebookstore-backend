package com.example.demo.controller;

import com.example.demo.service.TimerService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Scope("session")
@RequestMapping("/users")
public class LoginController {
    private final UserService userService;
    private final TimerService timerService;

    @Autowired
    public LoginController(UserService userService, TimerService timerService) {
        this.userService = userService;
        this.timerService = timerService;
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> payload) {
        String account = payload.get("account");
        String password = payload.get("password");

        // 在登录时启动计时器
        timerService.startTimer();

        return userService.login(account, password);
    }

    @PostMapping("/logout")
    public String logout() {
        // 在登出时停止计时器并获取计时值
        long elapsedTime = timerService.stopTimer();
        // 返回计时值给前端
        System.out.println("Session duration: " + elapsedTime + " milliseconds");
        return "You have been logged out. Session duration: " + elapsedTime + " milliseconds";
    }
}
