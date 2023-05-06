package com.example.demo.entity;

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
    public Item(String id, String title, String amount, String price, String createtime) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.price = price;
        this.createtime = createtime;
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

    @Override
    public String toString() {
        return String.format(
                "{ cover: '%s', title: '%s', amount: '%s', price: '%s' }",
                id, title, amount, price);
    }

}
