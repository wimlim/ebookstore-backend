package com.example.demo.entity;

public class List {

    private String cover;
    private String title;
    private String amount;
    private String price;

    public List(String cover, String title, String amount, String price) {
        this.cover = cover;
        this.title = title;
        this.amount = amount;
        this.price = price;
    }

    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    @Override
    public String toString() {
        return String.format(
                "{ cover: '%s', title: '%s', amount: '%s', price: '%s' }",
                cover, title, amount, price);
    }

}
