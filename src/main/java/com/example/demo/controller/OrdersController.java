package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Item;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;

    @Autowired
    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable("id") long userId) {
        JSONArray timestampsJson = orderService.getOrderItemsWithTimestamps(userId);
        return timestampsJson.toJSONString();
    }


    @PutMapping("/{userId}")
    public String addOrder(@PathVariable Long userId, @RequestParam Long bookId) {
        boolean success = orderService.addOrderItem(userId, bookId);
        if (success) {
            return "New order item added successfully.";
        } else {
            return "Failed to add new order item.";
        }
    }
}
