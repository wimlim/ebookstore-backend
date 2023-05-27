package com.example.demo.service;

import com.example.demo.entity.Book;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

// ...

@Service
public class BookService {

    @PersistenceContext
    private EntityManager entityManager;

    public String getAllBooks() {
        String jpql = "SELECT b FROM Book b";
        TypedQuery<Book> query = entityManager.createQuery(jpql, Book.class);
        List<Book> books = query.getResultList();

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
        Book book = entityManager.find(Book.class, Integer.parseInt(id));

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
        Book book = entityManager.find(Book.class, Integer.parseInt(id));
        if (book != null) {
            return book.getCover();
        }
        return null;
    }

}
