package com.example.demo.dao;

import com.example.demo.entity.UserAuth;

public interface UserAuthDao {
    UserAuth findByToken(int token);

    void save(UserAuth userAuth);
}
