package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.UserAuth;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/avatar/{token}")
    public ResponseEntity<byte[]> getUserAvatar(@PathVariable("token") Long token) {
        byte[] avatar = userService.getUserAvatar(token);
        if (avatar != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(avatar, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/profile/{token}")
    public String updateUserProfile(
            @PathVariable("token") Long token,
            @RequestBody Map<String, String> payload
    ) {
        String firstname = payload.get("firstname");
        String lastname = payload.get("lastname");
        String twitter = payload.get("twitter");
        String notes = payload.get("notes");

        return userService.updateUserProfile(token, firstname, lastname, twitter, notes);
    }

    @PutMapping("/avatar/{token}")
    public String updateUserAvatar(
            @PathVariable("token") Long token,
            @RequestBody byte[] avatar
    ) {
        return userService.updateUserAvatar(token, avatar);
    }
}
