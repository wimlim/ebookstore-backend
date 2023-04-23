package com.example.demo.entity;

public class Book {

    private Long id;

    private String title;
    private String author;
    private String language;
    private String published;
    private String price;
    private String status;
    private String description;

    public Book(Long id, String title, String author, String language, String published, String price, String status, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.language = language;
        this.published = published;
        this.price = price;
        this.status = status;
        this.description = description;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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

    @Override
    public String toString() {
        return String.format(
                "Book[id=%d, title='%s', author='%s', language='%s', published='%s', sales='%s']",
                id, title, author, language, published, price);
    }

}
