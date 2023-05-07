package com.example.demo.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Item;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;

@RestController
public class OrdersController {
    @RequestMapping("/orders/{id}")
    public String getOrder(@PathVariable("id") long userId) {
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Timestamp> timestamps = new ArrayList<>();

        try{
            // Create a connection to MySQL database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false", "root", "sql.14159265");

            // Query for retrieving all data from orders and orderitems tables based on user id
            String sql = "SELECT DISTINCT orders.createtime, books.id, books.title, orderitems.num, books.price FROM orders JOIN orderitems ON orders.id = orderitems.order_id JOIN books ON orderitems.book_id = books.id WHERE orders.user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Extract data from result set
                Timestamp createTime = resultSet.getTimestamp("createtime");
                if(!timestamps.contains(createTime)) {
                    timestamps.add(createTime);
                }
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                int amount = resultSet.getInt("num");
                double price = resultSet.getDouble("price");
                Item item = new Item(String.valueOf(id), title, String.valueOf(amount), String.valueOf(price));
                item.setCreatetime(createTime.toString());
                items.add(item);
            }

            // Close the connection
            resultSet.close();
            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray timestampsJson = new JSONArray();
        for(Timestamp t : timestamps) {
            JSONObject timestampJson = new JSONObject();
            // create a JSON array for items
            ArrayList<JSONArray> itemListJson = new ArrayList<>();
            for (Item i: items) {
                if(i.getCreatetime().equals(t.toString())) {
                    itemListJson.add(i.toJson());
                }
            }
            timestampJson.put("timestamp", t.toString());
            timestampJson.put("items", itemListJson);
            timestampsJson.add(timestampJson);
        }
        return timestampsJson.toJSONString();
    }

    @PutMapping("/orders/{userId}")
    public String addOrder(@PathVariable Long userId, @RequestParam Long bookId) {
        // Create a connection to MySQL database
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false", "root", "sql.14159265")) {
            int nextId = 0;
            PreparedStatement stmt = conn.prepareStatement("SELECT MAX(id) FROM orders");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nextId = rs.getInt(1) + 1;
            }

            // Insert into orders table
            String insertOrderSql = "INSERT INTO orders (id, user_id, createtime) VALUES (?, ?, ?)";
            PreparedStatement orderStatement = conn.prepareStatement(insertOrderSql);
            orderStatement.setLong(1, nextId);
            orderStatement.setLong(2, userId);
            orderStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            orderStatement.executeUpdate();

            // Insert into orderitems table
            String insertOrderItemSql = "INSERT INTO orderitems (order_id, book_id, num) VALUES (?, ?, ?)";
            PreparedStatement orderItemStatement = conn.prepareStatement(insertOrderItemSql);
            orderItemStatement.setLong(1, nextId);
            orderItemStatement.setLong(2, bookId);
            orderItemStatement.setInt(3, 1);
            orderItemStatement.executeUpdate();

            return "New order item added successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to add new order item.";
        }
    }



}

