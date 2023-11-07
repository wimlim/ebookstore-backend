package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;

public interface OrderService {
    JSONArray getOrderItemsWithTimestamps(int token);

    JSONArray getAllOrders();

    boolean addOrderItem(int token, int bookId);
}
