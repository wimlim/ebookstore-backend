package com.example.demo.dao.impl;

import com.example.demo.dao.UserAuthDao;
import com.example.demo.entity.UserAuth;
import com.example.demo.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserAuthDaoImpl implements UserAuthDao {

    private final UserAuthRepository userAuthRepository;

    @Autowired
    public UserAuthDaoImpl(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public UserAuth findByToken(int token) {
        return userAuthRepository.findByToken(token);
    }

    @Override
    public void save(UserAuth userAuth) {
        userAuthRepository.save(userAuth);
    }
}
