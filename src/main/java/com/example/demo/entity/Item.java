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
