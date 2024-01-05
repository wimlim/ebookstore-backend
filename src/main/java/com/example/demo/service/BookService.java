package com.example.demo.service;

import com.example.demo.entity.Book;

import java.util.List;

public interface BookService {

    String getAllBooks();

    String getBooksByType(String type);

    List<Book> getBooksByTitle(String title);

    String getBookById(int id);

    byte[] getBookCoverData(int id);

    void updateBook(int id, String updatedBook);
    void deleteBook(int id);

    void addBook(String newBook);
}
