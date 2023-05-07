package com.example.demo.entity;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;

public class User {
    private String firstname;
    private String lastname;
    private String twitter;
    private String notes;
    public User(String firstname, String lastname, String twitter, String notes) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.twitter = twitter;
        this.notes = notes;
    }

    public JSONArray toJson() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(firstname);
        arrayList.add(lastname);
        arrayList.add(twitter);
        arrayList.add(notes);
        return (JSONArray) JSONArray.toJSON(arrayList);
    }
}
