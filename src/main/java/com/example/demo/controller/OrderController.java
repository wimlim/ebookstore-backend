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

    @GetMapping("/{token}")
    public String getOrder(@PathVariable("token") int token) {
        JSONArray timestampsJson = orderService.getOrderItemsWithTimestamps(token);
        return timestampsJson.toJSONString();
    }


    @PutMapping("/{token}")
    public String addOrder(@PathVariable int token, @RequestParam int bookId) {
        boolean success = orderService.addOrderItem(token, bookId);
        if (success) {
            return "New order item added successfully.";
        } else {
            return "Failed to add new order item.";
        }
    }
}
