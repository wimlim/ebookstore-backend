package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.User;
import com.example.demo.entity.UserAuth;
import com.example.demo.repository.UserAuthRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserAuthRepository userAuthRepository) {
        this.userRepository = userRepository;
        this.userAuthRepository = userAuthRepository;
    }

    public String login(String account, String password) {
        Optional<User> userOptional = userRepository.findByAccountAndPassword(account, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<UserAuth> userAuthOptional = userAuthRepository.findByUser(user);
            if (userAuthOptional.isPresent()) {
                UserAuth userAuth = userAuthOptional.get();
                return userAuth.getToken();
            }
        }
        return "";
    }

    public String getUserProfile(String token) {
        Optional<UserAuth> userAuthOptional = userAuthRepository.findByToken(token);
        if (userAuthOptional.isPresent()) {
            UserAuth userAuth = userAuthOptional.get();
            User user = userAuth.getUser();
            return convertUserToJsonString(user);
        } else {
            return "User not found";
        }
    }

    private String convertUserToJsonString(User user) {
        User[] users = {user};
        return JSONArray.toJSONString(users, SerializerFeature.BrowserCompatible);
    }
}
