package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.User;
import com.example.demo.entity.UserAuth;
import com.example.demo.repository.UserAuthRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
                return Integer.toString(userAuth.getToken());
            }
        }
        return "";
    }

    public String getUserProfile(Long token) {
        Optional<UserAuth> userAuthOptional = userAuthRepository.findByToken(token.intValue());
        if (userAuthOptional.isPresent()) {
            UserAuth userAuth = userAuthOptional.get();
            User user = userAuth.getUser();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("firstname", user.getFirstName());
            jsonObject.put("lastname", user.getLastName());
            jsonObject.put("twitter", user.getTwitter());
            jsonObject.put("notes", user.getNotes());

            return jsonObject.toString();
        } else {
            return "";
        }
    }


}
