package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.UserDao;
import com.example.demo.dao.UserAuthDao;
import com.example.demo.entity.User;
import com.example.demo.entity.UserAuth;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserAuthDao userAuthDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, UserAuthDao userAuthDao) {
        this.userDao = userDao;
        this.userAuthDao = userAuthDao;
    }

    @Override
    public String login(String account, String password) {
        User user = userDao.findByAccountAndPassword(account, password);
        if (user != null) {
            if (user.getIsBanned()) {
                return "banned";
            }
            UserAuth userAuth = user.getUserAuth();
            boolean isAdmin = user.isAdmin();
            return userAuth.getToken() + "," + isAdmin;
        }
        return "";
    }

    @Override
    public String getUserProfile(Long token) {
        UserAuth userAuth = userAuthDao.findByToken(token.intValue());
        if (userAuth != null) {
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
    @Override
    public String getAllUsers() {
        List<User> users = userDao.findAll(); // 查询所有用户

        JSONArray jsonArray = new JSONArray();
        for (User user : users) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", user.getId());
            jsonObject.put("account", user.getAccount());
            jsonObject.put("is_admin", user.isAdmin());
            jsonObject.put("is_banned", user.getIsBanned());
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }
    @Override
    public String banUser(Long userId) {
        User user = userDao.findById(userId);
        if (user != null) {
            user.setIsBanned(true);
            userDao.save(user);
            return "User banned successfully";
        }
        return "User not found";
    }
    @Override
    public String unbanUser(Long userId) {
        User user = userDao.findById(userId);
        if (user != null) {
            user.setIsBanned(false);
            userDao.save(user);
            return "User unbanned successfully";
        }
        return "User not found";
    }
}
