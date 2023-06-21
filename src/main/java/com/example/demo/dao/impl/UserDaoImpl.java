package com.example.demo.dao.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;

    @Autowired
    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByAccountAndPassword(String account, String password) {
        return userRepository.findByAccountAndPassword(account, password);
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    @Override
    public void save(User user) {
        userRepository.save(user);
    }
    @Override
    public com.example.demo.entity.User findByAccount(String username) {
        return userRepository.findByAccount(username);
    }
}
