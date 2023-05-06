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
    private String cover;

    public Book(String id, String title, String author, String language, String published, String price, String status, String description, String cover) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.language = language;
        this.published = published;
        this.price = price;
        this.status = status;
        this.description = description;
        this.cover = cover;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublished() {
        return published;
    }
    public void setPublished(String published) {
        this.published = published;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String sales) {
        this.price = sales;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCover() {
        return cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
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
        arrayList.add(cover);
        return (JSONArray) JSONArray.toJSON(arrayList);
    }
}
