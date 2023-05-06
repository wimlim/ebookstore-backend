package com.example.demo.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.Item;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;

@RestController
public class ListsController {
    @RequestMapping("/lists/{userid}")
    public String getList(@PathVariable("userid") long userId) {
        ArrayList<Item> items = new ArrayList<>();

        try{
            // 创建与MySQL数据库的连接
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false", "root", "sql.14159265");

            // 创建要执行的SQL查询语句，用于从listitems和books表中获取所有数据
            String sql = "SELECT books.id, books.title, listitems.amount, books.price FROM listitems JOIN books ON listitems.book_id = books.id WHERE listitems.user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            // 遍历结果集，构建list对象列表
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                int amount = resultSet.getInt("amount");
                double price = resultSet.getDouble("price");
                Item item = new Item(String.valueOf(id), title, String.valueOf(amount), String.valueOf(price));
                items.add(item);
            }

            // 关闭连接
            resultSet.close();
            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<JSONArray> listJson = new ArrayList<>();
        for (Item i: items) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(String.valueOf(i.getId()));
            arrayList.add(i.getTitle());
            arrayList.add(String.valueOf(i.getAmount()));
            arrayList.add(String.valueOf(i.getPrice()));
            listJson.add((JSONArray) JSONArray.toJSON(arrayList));
        }
        return JSONArray.toJSONString(listJson, SerializerFeature.BrowserCompatible);
    }

    @PutMapping("/lists/{userId}")
    public String updateList(@PathVariable Long userId, @RequestParam Long bookId, @RequestParam Long amount) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false", "root", "sql.14159265")) {
            String sql = "SELECT * FROM listitems WHERE user_id = ? AND book_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, userId);
                statement.setLong(2, bookId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        sql = "UPDATE listitems SET amount=? WHERE user_id=? AND book_id=?";
                        try (PreparedStatement updateStatement = conn.prepareStatement(sql)) {
                            updateStatement.setLong(1, amount);
                            updateStatement.setLong(2, userId);
                            updateStatement.setLong(3, bookId);
                            updateStatement.executeUpdate();
                            return "Record updated successfully!";
                        }
                    } else {
                        sql = "INSERT INTO listitems(user_id, book_id, amount) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStatement = conn.prepareStatement(sql)) {
                            insertStatement.setLong(1, userId);
                            insertStatement.setLong(2, bookId);
                            insertStatement.setLong(3, amount);
                            insertStatement.executeUpdate();
                            return "Record created successfully!";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while updating the record!";
        }
    }

    @DeleteMapping("/lists/{id}")
    public String deleteList(@PathVariable Long id, @RequestParam Long book_id) {
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false", "root", "sql.14159265");
            String sql = "DELETE FROM listitems WHERE user_id = ? AND book_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            statement.setLong(2, book_id);
            int count = statement.executeUpdate();

            statement.close();
            conn.close();

            if (count > 0) {
                return "Record deleted successfully!";
            } else {
                return "No record found!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while deleting the record!";
        }
    }

    @PostMapping("/lists/{id}")
    public String purchaseList(@PathVariable Long id) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false", "root", "sql.14159265")) {
            // Retrieve all items in the user's list
            String sql = "SELECT * FROM listitems WHERE user_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return "No record found!";
                    }
                    else {
                        // Create a new order
                        sql = "INSERT INTO orders(userid, createtime) VALUES (?, NOW())";
                        try (PreparedStatement insertOrderStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                            insertOrderStatement.setLong(1, id);
                            insertOrderStatement.executeUpdate();
                            ResultSet generatedKeys = insertOrderStatement.getGeneratedKeys();
                            if (generatedKeys.next()) {
                                long orderId = generatedKeys.getLong(1);

                                // Insert new order items for each item in the user's list
                                do {
                                    long bookId = resultSet.getLong("book_id");
                                    int amount = resultSet.getInt("amount");
                                    sql = "INSERT INTO orderitems(order_id, book_id, num) VALUES (?, ?, ?)";
                                    try (PreparedStatement insertOrderItemStatement = conn.prepareStatement(sql)) {
                                        insertOrderItemStatement.setLong(1, orderId);
                                        insertOrderItemStatement.setLong(2, bookId);
                                        insertOrderItemStatement.setInt(3, amount);
                                        insertOrderItemStatement.executeUpdate();
                                    }
                                } while (resultSet.next());

                                // Delete all items in the user's list
                                sql = "DELETE FROM listitems WHERE user_id = ?";
                                try (PreparedStatement deleteListStatement = conn.prepareStatement(sql)) {
                                    deleteListStatement.setLong(1, id);
                                    deleteListStatement.executeUpdate();
                                }

                                return "Purchase successfully!";
                            }
                            else {
                                return "Failed to create order!";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while purchasing the items!";
        }
    }

}
