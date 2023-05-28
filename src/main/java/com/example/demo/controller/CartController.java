package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.Item;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/lists")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{token}")
    public String getCart(@PathVariable("token") long token) {
        return cartService.getList(token);
    }

    @PutMapping("/{token}")
    public String updateCart(@PathVariable("token") long token, @RequestParam("bookId") long bookId, @RequestParam("amount") long amount) {
        if (cartService.updateItem(token, bookId, amount)) {
            return "Record updated successfully!";
        } else {
            return "Error occurred while updating the record!";
        }
    }

    @DeleteMapping("/{token}")
    public String deleteCart(@PathVariable("token") long token, @RequestParam("bookId") long bookId) {
        if (cartService.deleteItem(token, bookId)) {
            return "Record deleted successfully!";
        } else {
            return "Error occurred while deleting the record!";
        }
    }

    @PostMapping("/{token}")
    public String purchaseCart(@PathVariable("token") long token) {
        if (cartService.purchaseList(token)) {
            return "Purchase successfully!";
        } else {
            return "Error occurred while purchasing the items!";
        }
    }

}
