package com.example.demo.controller;

import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // 注入KafkaTemplate

    private static final String ORDER_MESSAGE_TOPIC = "order-message-topic"; // Kafka主题名称

    @GetMapping("/{token}")
    public String getCart(@PathVariable("token") int token) {
        return cartService.getList(token);
    }

    @GetMapping("/amount/{token}")
    public int getAmount(@PathVariable("token") int token) {
        return cartService.getAmount(token);
    }

    @PutMapping("/{token}")
    public String updateCart(@PathVariable("token") int token, @RequestParam("bookId") int bookId, @RequestParam("amount") int amount) {
        if (cartService.updateItem(token, bookId, amount)) {
            return "Record updated successfully!";
        } else {
            return "Error occurred while updating the record!";
        }
    }

    @DeleteMapping("/{token}")
    public String deleteCart(@PathVariable("token") int token, @RequestParam("bookId") int bookId) {
        if (cartService.deleteItem(token, bookId)) {
            return "Record deleted successfully!";
        } else {
            return "Error occurred while deleting the record!";
        }
    }
    @PostMapping("/{token}")
    public String purchaseCart(@PathVariable("token") int token) {
        kafkaTemplate.send(ORDER_MESSAGE_TOPIC, String.valueOf(token));
        System.out.println("Send " + token + " to Kafka");
        return "Purchase successfully!";
    }
}