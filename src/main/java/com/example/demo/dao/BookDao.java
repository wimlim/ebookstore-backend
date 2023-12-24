package com.example.demo.dao;

import com.example.demo.entity.Book;

import java.util.List;

public interface BookDao {
    List<Book> findAll();

    List<Book> findByTitle(String title);

    Book findById(int id);

    byte[] getCoverData(int id);

    void update(Book book);
    void delete(Book book);

    void updateStock(int bookId, int i);

    void save(Book book);

    int getMaxId();
}

