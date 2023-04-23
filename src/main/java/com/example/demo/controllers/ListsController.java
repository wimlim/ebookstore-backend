package com.example.demo.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.List;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ListsController {
    private ArrayList<List> lists; // 将lists变量定义为成员变量

    public ListsController() {
        lists = new ArrayList<>();
        List list1 = new List("1", "The Lord of the Rings", "2", "150");
        List list2 = new List("6", "The Hobbit", "1", "100");
        List list3 = new List("8", "Spore", "2", "100");
        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
    }

    @RequestMapping("/lists")
    public String home() {
        ArrayList<JSONArray> listsJson = new ArrayList<JSONArray>();
        for (List l : lists) {
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add(l.getCover());
            arrayList.add(l.getTitle());
            arrayList.add(l.getAmount());
            arrayList.add(l.getPrice());
            listsJson.add((JSONArray) JSONArray.toJSON(arrayList));
        }
        String listsString = JSONArray.toJSONString(listsJson, SerializerFeature.BrowserCompatible);

        return listsString;
    }

    @PutMapping("/lists/{id}") // 处理PUT请求
    public String updateList(@PathVariable String id, @RequestBody List updatedList) {
        // 根据id查找要更新的记录
        List listToUpdate = findListById(id);
        // 更新记录
        listToUpdate.setCover(updatedList.getCover());
        listToUpdate.setTitle(updatedList.getTitle());
        listToUpdate.setAmount(updatedList.getAmount());
        listToUpdate.setPrice(updatedList.getPrice());
        return "Record updated successfully!";
    }

    @DeleteMapping("/lists/{id}") // 处理DELETE请求
    public String deleteList(@PathVariable String id) {
        // 根据id查找要删除的记录
        List listToDelete = findListById(id);
        // 从列表中删除记录
        lists.remove(listToDelete);
        return "Record deleted successfully!";
    }

    @PostMapping("/lists/purchase") // 处理POST请求
    public String purchaseList(@RequestBody List purchasedList) {
        // 购买记录
        int amount = Integer.parseInt(purchasedList.getAmount());
        int price = Integer.parseInt(purchasedList.getPrice());
        int totalCost = amount * price;
        System.out.println("Received purchase request: " + purchasedList.getTitle() + ", amount: " + purchasedList.getAmount() + ", price: " + purchasedList.getPrice() + ", total cost: " + totalCost);
        return "You have successfully purchased " + purchasedList.getTitle() + " for a total cost of " + totalCost + "!";
    }


    private List findListById(String id) {
        for (List l : lists) {
            if (l.getCover().equals(id)) {
                return l;
            }
        }
        return null; // 没有找到对应的记录
    }
}
