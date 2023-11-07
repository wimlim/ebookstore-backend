package com.example.demo.service.impl;

import com.example.demo.dao.BookDao;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.plaf.synth.SynthTextAreaUI;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    @Autowired
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public String getAllBooks() {
        List<Book> books = bookDao.findAll();

        JSONArray booksJson = new JSONArray();
        for (Book book : books) {
            JSONObject bookJson = new JSONObject();
            bookJson.put("id", book.getId());
            bookJson.put("title", book.getTitle());
            bookJson.put("author", book.getAuthor());
            bookJson.put("language", book.getLanguage());
            bookJson.put("isbn", book.getIsbn());
            bookJson.put("price", book.getPrice());
            bookJson.put("stock", book.getStock());
            bookJson.put("description", book.getDescription());
            booksJson.add(bookJson);
        }
        return booksJson.toJSONString();
    }

    @Override
    public String getBookById(int id) {
        Book book = bookDao.findById(id);

        if (book != null) {
            JSONObject bookJson = new JSONObject();
            bookJson.put("id", book.getId());
            bookJson.put("title", book.getTitle());
            bookJson.put("author", book.getAuthor());
            bookJson.put("language", book.getLanguage());
            bookJson.put("isbn", book.getIsbn());
            bookJson.put("price", book.getPrice());
            bookJson.put("stock", book.getStock());
            bookJson.put("description", book.getDescription());
            return bookJson.toJSONString();
        }
        return null;
    }

    @Override
    public byte[] getBookCoverData(int id) {
        return bookDao.getCoverData(id);
    }

    @Override
    public void updateBook(int id, String updatedBook) {
        Book existingBook = bookDao.findById(id);

        if (existingBook == null) {
            throw new IllegalArgumentException("Book not found");
        }

        JSONObject bookJson = JSONObject.parseObject(updatedBook); // 使用fastjson将字符串转换为JSON对象

        existingBook.setTitle(bookJson.getString("title"));
        existingBook.setAuthor(bookJson.getString("author"));
        existingBook.setLanguage(bookJson.getString("language"));
        existingBook.setIsbn(bookJson.getInteger("isbn"));
        existingBook.setPrice(bookJson.getInteger("price"));
        existingBook.setStock(bookJson.getInteger("stock"));
        existingBook.setDescription(bookJson.getString("description"));

        bookDao.update(existingBook);
    }

    @Override
    public void deleteBook(int id) {
        Book existingBook = bookDao.findById(id);

        if (existingBook == null) {
            throw new IllegalArgumentException("Book not found");
        }

        bookDao.delete(existingBook);
    }

    @Override
    public void addBook(String newBook) {
        JSONObject bookJson = JSONObject.parseObject(newBook); // 使用fastjson将字符串转换为JSON对象

        Book book = new Book();
        System.out.println("in addBook");
        book.setTitle(bookJson.getString("title"));
        System.out.println("1");
        book.setAuthor(bookJson.getString("author"));
        book.setLanguage(bookJson.getString("language"));
        System.out.println("2");
        book.setIsbn(bookJson.getInteger("isbn"));
        System.out.println("3");
        book.setPrice(bookJson.getInteger("price"));
        book.setStock(bookJson.getInteger("stock"));
        book.setDescription(bookJson.getString("description"));
        System.out.println("out addBook");
        bookDao.save(book);
    }
}