package com.example.demo.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    // POST请求 "/login"
    @PostMapping("/login")
    public int login(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String password = payload.get("password");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false&allowPublicKeyRetrieval=true", "root", "sql.14159265");
             PreparedStatement statement = conn.prepareStatement("SELECT id FROM users WHERE name=? AND password=?")) {

            statement.setString(1, name);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                resultSet.close();
                return userId;
            } else {
                resultSet.close();
                return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}

