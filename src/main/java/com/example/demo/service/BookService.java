package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public String getAllBooks() {
        List<Book> books = bookRepository.findAll();

        JSONArray booksJson = new JSONArray();
        for (Book book : books) {
            JSONObject bookJson = new JSONObject();
            bookJson.put("id", book.getId());
            bookJson.put("title", book.getTitle());
            bookJson.put("author", book.getAuthor());
            bookJson.put("language", book.getLanguage());
            bookJson.put("published", book.getPublished());
            bookJson.put("price", book.getPrice());
            bookJson.put("status", book.getStatus());
            bookJson.put("description", book.getDescription());
            booksJson.add(bookJson);
        }
        return booksJson.toJSONString();
    }

    public String getBookById(String id) {
        int bookId = Integer.parseInt(id);
        Book book = bookRepository.findById(bookId).orElse(null);

        if (book != null) {
            JSONObject bookJson = new JSONObject();
            bookJson.put("id", book.getId());
            bookJson.put("title", book.getTitle());
            bookJson.put("author", book.getAuthor());
            bookJson.put("language", book.getLanguage());
            bookJson.put("published", book.getPublished());
            bookJson.put("price", book.getPrice());
            bookJson.put("status", book.getStatus());
            bookJson.put("description", book.getDescription());
            return bookJson.toJSONString();
        }
        return null;
    }

    public byte[] getBookCoverData(String id) {
        int bookId = Integer.parseInt(id);
        Book book = bookRepository.findById(bookId).orElse(null);

        if (book != null) {
            return book.getCover();
        }
        return null;
    }

}
