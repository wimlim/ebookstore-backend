package com.example.demo.service;

import com.alibaba.fastjson.JSON;
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

    public String getBookById(int id) {
        Book book = bookRepository.findById(id).orElse(null);

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

    public byte[] getBookCoverData(int id) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book != null) {
            return book.getCover();
        }
        return null;
    }

    public void updateBook(int id, String updatedBook) {
        Book existingBook = bookRepository.findById(id).orElse(null);

        if (existingBook == null) {
            throw new IllegalArgumentException("Book not found");
        }

        JSONObject bookJson = JSONObject.parseObject(updatedBook); // 使用fastjson将字符串转换为JSON对象

        existingBook.setTitle(bookJson.getString("title"));
        existingBook.setAuthor(bookJson.getString("author"));
        existingBook.setLanguage(bookJson.getString("language"));
        existingBook.setPublished(bookJson.getInteger("published"));
        existingBook.setPrice(bookJson.getInteger("price"));
        existingBook.setStatus(bookJson.getString("status"));
        existingBook.setDescription(bookJson.getString("description"));

        bookRepository.save(existingBook);
    }
}