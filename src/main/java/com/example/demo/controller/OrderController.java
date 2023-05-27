package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
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
