package com.example.demo.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.User;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;


@RestController
public class UserController {
    // POST请求 "/login"
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> payload) {
        String account = payload.get("account");
        String password = payload.get("password");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false&allowPublicKeyRetrieval=true", "root", "sql.14159265");
             PreparedStatement statement = conn.prepareStatement("SELECT user_id, hash FROM userauths WHERE account=? AND password=?")) {

            statement.setString(1, account);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String hash = resultSet.getString("hash");
                String id = resultSet.getString("user_id");
                resultSet.close();
                return id;
            } else {
                resultSet.close();
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping("/profile/{userid}")
    public String profile(@PathVariable Long userid) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false&allowPublicKeyRetrieval=true", "root", "sql.14159265");
             PreparedStatement statement = conn.prepareStatement("SELECT firstname, lastname, twitter, notes FROM users WHERE id=?")) {

            statement.setLong(1, userid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String twitter = resultSet.getString("twitter");
                String notes = resultSet.getString("notes");

                // create a JSON object for user information
                User user = new User(firstname, lastname, twitter, notes);
                // convert user information to JSON string
                ArrayList<JSONArray> userJson = new ArrayList<JSONArray>();
                userJson.add(user.toJson());
                return JSONArray.toJSONString(userJson, SerializerFeature.BrowserCompatible);
            } else {
                resultSet.close();
                return "User not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving user information";
        }
    }
}
