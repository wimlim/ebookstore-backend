package com.example.demo.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;

public class Book {

    private String id;

    private String title;
    private String author;
    private String language;
    private String published;
    private String price;
    private String status;
    private String description;

    public Book(String id, String title, String author, String language, String published, String price, String status, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.language = language;
        this.published = published;
        this.price = price;
        this.status = status;
        this.description = description;
    }

    public JSONArray toJson() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(id);
        arrayList.add(title);
        arrayList.add(author);
        arrayList.add(language);
        arrayList.add(published);
        arrayList.add(price);
        arrayList.add(status);
        arrayList.add(description);
        return (JSONArray) JSONArray.toJSON(arrayList);
    }
}
