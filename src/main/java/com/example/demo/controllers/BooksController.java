package com.example.demo.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.Book;
import com.example.demo.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String getBooks() {
        ArrayList<Book> books = bookService.getAllBooks();
        ArrayList<JSONArray> booksJson = new ArrayList<JSONArray>();
        for (Book b : books) {
            booksJson.add(b.toJson());
        }
        return JSONArray.toJSONString(booksJson, SerializerFeature.BrowserCompatible);
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable String id) {
        Book book = bookService.getBookById(id);
        ArrayList<JSONArray> booksJson = new ArrayList<JSONArray>();
        booksJson.add(book.toJson());
        return JSONArray.toJSONString(booksJson, SerializerFeature.BrowserCompatible);
    }
}
