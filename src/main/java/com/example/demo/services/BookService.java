package com.example.demo.services;

import com.example.demo.entity.Book;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Service
public class BookService {

    private static final String URL = "jdbc:mysql://localhost/bookstore?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "sql.14159265";

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM books";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String language = resultSet.getString("language");
                String published = resultSet.getString("published");
                String price = resultSet.getString("price");
                String status = resultSet.getString("status");
                String description = resultSet.getString("description");
                Book book = new Book(id, title, author, language, published, price, status, description);
                books.add(book);
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public Book getBookById(String id) {
        Book book = null;
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM books WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String language = resultSet.getString("language");
                String published = resultSet.getString("published");
                String price = resultSet.getString("price");
                String status = resultSet.getString("status");
                String description = resultSet.getString("description");
                book = new Book(id, title, author, language, published, price, status, description);
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }
}
