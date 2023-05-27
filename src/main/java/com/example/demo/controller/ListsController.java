package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.Item;
import com.example.demo.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/lists")
public class ListsController {

    @Autowired
    private ListService listService;

    @GetMapping("/{userId}")
    public String getList(@PathVariable("userId") long userId) {
        ArrayList<Item> items = listService.getList(userId);
        ArrayList<JSONArray> listJson = new ArrayList<>();
        for (Item i: items) {
            listJson.add(i.toJson());
        }
        return JSONArray.toJSONString(listJson, SerializerFeature.BrowserCompatible);
    }

    @PutMapping("/{userId}")
    public String updateList(@PathVariable("userId") long userId, @RequestParam("bookId") long bookId, @RequestParam("amount") long amount) {
        if (listService.updateItem(userId, bookId, amount)) {
            return "Record updated successfully!";
        } else {
            return "Error occurred while updating the record!";
        }
    }

    @DeleteMapping("/{userId}")
    public String deleteList(@PathVariable("userId") long userId, @RequestParam("bookId") long bookId) {
        if (listService.deleteItem(userId, bookId)) {
            return "Record deleted successfully!";
        } else {
            return "Error occurred while deleting the record!";
        }
    }

    @PostMapping("/{userId}")
    public String purchaseList(@PathVariable("userId") long userId) {
        if (listService.purchaseList(userId)) {
            return "Purchase successfully!";
        } else {
            return "Error occurred while purchasing the items!";
        }
    }

}
