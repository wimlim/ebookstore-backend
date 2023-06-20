package com.example.demo.dao;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;

import java.util.List;

public interface OrderDao {
    List<Order> findByUser(User user);
    void saveOrder(Order order);

    List<Order> findAll();
}
