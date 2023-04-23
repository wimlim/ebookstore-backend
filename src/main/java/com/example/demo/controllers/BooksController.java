package com.example.demo.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.Book;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class BooksController {
    @RequestMapping("/books")
    public String home() {
        ArrayList<Book> books = new ArrayList<>();
        Book book1 = new Book(1L, "The Lord of the Rings", "J. R. R. Tolkien", "English", "1954-1955", "$150", "On Sale", "asdf");
        Book book2 = new Book(2L, "The Lord of the Rings", "J. R. R. Tolkien", "English", "1954-1955", "$150", "On Sale", "asdf");
        books.add(book1);
        books.add(book2);

        ArrayList<JSONArray> booksJson = new ArrayList<JSONArray>();
        for (Book b : books) {
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add(String.valueOf(b.getId()));
            arrayList.add(b.getTitle());
            arrayList.add(b.getAuthor());
            arrayList.add(b.getLanguage());
            arrayList.add(b.getPublished());
            arrayList.add(b.getPrice());
            arrayList.add(b.getStatus());
            arrayList.add(b.getDescription());
            booksJson.add((JSONArray) JSONArray.toJSON(arrayList));
        }
        String booksString = JSONArray.toJSONString(booksJson, SerializerFeature.BrowserCompatible);

        return booksString;
    }
}
