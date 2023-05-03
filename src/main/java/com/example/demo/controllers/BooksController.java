package com.example.demo.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.Book;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@RestController
public class BooksController {
    @RequestMapping("/books")
    public String home() {
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
                long id = resultSet.getLong("id");
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

            // 关闭连接
            resultSet.close();
            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

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
