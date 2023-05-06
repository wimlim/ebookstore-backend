package com.example.demo.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.Book;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;

@RestController
public class BooksController {
    @RequestMapping("/books")
    public String getBooks() {
        ArrayList<Book> books = new ArrayList<>();
        try{
            // 创建与MySQL数据库的连接
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false", "root", "sql.14159265");

            // 创建要执行的SQL查询语句，用于从books表中获取所有数据
            String sql = "SELECT * FROM books";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // 遍历结果集，构建Book对象列表
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String language = resultSet.getString("language");
                String published = resultSet.getString("published");
                String price = resultSet.getString("price");
                String status = resultSet.getString("status");
                String description = resultSet.getString("description");

                byte[] coverData = resultSet.getBytes("cover");
                String cover = Base64.getEncoder().encodeToString(coverData);

                Book book = new Book(id, title, author, language, published, price, status, description, "");
                books.add(book);
            }
            // 关闭连接
            resultSet.close();
            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<JSONArray> booksJson = new ArrayList<JSONArray>();
        for (Book b : books) {
            booksJson.add(b.toJson());
        }
        return JSONArray.toJSONString(booksJson, SerializerFeature.BrowserCompatible);
    }

    @GetMapping("/books/{id}")
    public String getBook(@PathVariable String id) {
        Book book = null;
        try {
            // 创建与MySQL数据库的连接
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false", "root", "sql.14159265");

            // 创建要执行的SQL查询语句，用于从books表中获取指定id的数据
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

                byte[] coverData = resultSet.getBytes("cover");
                String cover = Base64.getEncoder().encodeToString(coverData);

                book = new Book(id, title, author, language, published, price, status, description, "");
            }

            // 关闭连接
            resultSet.close();
            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<JSONArray> booksJson = new ArrayList<JSONArray>();
        booksJson.add(book.toJson());
        return JSONArray.toJSONString(booksJson, SerializerFeature.BrowserCompatible);
    }
}
