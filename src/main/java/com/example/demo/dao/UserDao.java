package com.example.demo.dao;

import com.example.demo.entity.User;

import java.util.List;

public interface UserDao {
    User findByAccountAndPassword(String account, String password);

    List<User> findAll();

    User findById(Long userId);

    void save(User user);

    User findByAccount(String username);
}
