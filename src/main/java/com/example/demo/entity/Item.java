package com.example.demo.entity;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;

public class Item {

    private String id;
    private String title;
    private String amount;
    private String price;
    private String createtime;

    public Item(String id, String title, String amount, String price) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getCreatetime() { return createtime; }
    public void setCreatetime(String createtime) { this.createtime = createtime; }

    public JSONArray toJson() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(id);
        arrayList.add(title);
        arrayList.add(amount);
        arrayList.add(price);
        return (JSONArray) JSONArray.toJSON(arrayList);
    }

}
