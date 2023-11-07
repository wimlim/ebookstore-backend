package com.example.demo.service;

public interface BookService {

    String getAllBooks();

    String getBookById(int id);

    byte[] getBookCoverData(int id);

    void updateBook(int id, String updatedBook);
    void deleteBook(int id);

    void addBook(String newBook);
}
