package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserService {
    private static final String URL = "jdbc:mysql://localhost/bookstore?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "sql.14159265";

    public String login(String account, String password) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = conn.prepareStatement("SELECT user_id FROM userauths WHERE account=? AND password=?")) {

            statement.setString(1, account);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
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

    public String getUserProfile(Long userId) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = conn.prepareStatement("SELECT firstname, lastname, twitter, notes FROM users WHERE id=?")) {

            statement.setLong(1, userId);
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
