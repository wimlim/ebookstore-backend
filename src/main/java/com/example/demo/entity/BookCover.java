package com.example.demo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
@Document(collection = "bookCovers")
public class BookCover {
    @Id
    private int bookId;
    private byte[] bookCover;

    public byte[] getCover() {
        return bookCover;
    }
}