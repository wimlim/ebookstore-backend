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
    @Override
    public String register(String username, String password, String email) {
        User user = userDao.findByAccount(username);
        if (user != null) {
            return "Username already exists";
        }

        user = new User();
        user.setAccount(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setIsBanned(false);
        user.setIsAdmin(false);
        userDao.save(user);

        UserAuth userAuth = new UserAuth();
        userAuth.setUser(user);
        userAuth.setToken(); // Generate a random token
        userAuthDao.save(userAuth);

        return "Register successfully";
    }
    @Override
    public byte[] getUserAvatar(Long token) {
        UserAuth userAuth = userAuthDao.findByToken(token.intValue());
        if (userAuth != null) {
            User user = userAuth.getUser();
            return user.getAvatar();
        }
        return null;
    }
    @Override
    public String updateUserProfile(Long token, String firstname, String lastname, String twitter, String notes) {
        System.out.println("updateUserProfile");
        UserAuth userAuth = userAuthDao.findByToken(token.intValue());
        if (userAuth != null) {
            User user = userAuth.getUser();
            user.setFirstName(firstname);
            user.setLastName(lastname);
            user.setTwitter(twitter);
            user.setNotes(notes);
            userDao.save(user);
            return "User profile updated successfully";
        }
        return "User not found";
    }

    @Override
    public String updateUserAvatar(Long token, byte[] avatar) {
        UserAuth userAuth = userAuthDao.findByToken(token.intValue());
        if (userAuth != null) {
            User user = userAuth.getUser();
            user.setAvatar(avatar);
            userDao.save(user);
            return "User avatar updated successfully";
        }
        return "User not found";
    }
}
