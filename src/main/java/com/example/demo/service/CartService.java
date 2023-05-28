package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.Item;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class CartService {

    private static final String URL = "jdbc:mysql://localhost/bookstore?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "sql.14159265";

    public String getList(long token) {
        ArrayList<Item> items = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT books.id, books.title, cartitems.amount, books.price FROM cartitems JOIN books ON cartitems.book_id = books.id WHERE cartitems.user_id = (SELECT user_id FROM userauths WHERE token = ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, token);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        String title = resultSet.getString("title");
                        int amount = resultSet.getInt("amount");
                        double price = resultSet.getDouble("price");
                        Item item = new Item(String.valueOf(id), title, String.valueOf(amount), String.valueOf(price));
                        items.add(item);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<JSONArray> listJson = new ArrayList<>();
        for (Item i: items) {
            listJson.add(i.toJson());
        }
        return JSONArray.toJSONString(listJson, SerializerFeature.BrowserCompatible);
    }

    public boolean updateItem(long token, long bookId, long amount) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM cartitems WHERE user_id = (SELECT user_id FROM userauths WHERE token = ?) AND book_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, token);
                statement.setLong(2, bookId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        sql = "UPDATE cartitems SET amount=? WHERE user_id=(SELECT user_id FROM userauths WHERE token = ?) AND book_id=?";
                        try (PreparedStatement updateStatement = conn.prepareStatement(sql)) {
                            updateStatement.setLong(1, amount);
                            updateStatement.setLong(2, token);
                            updateStatement.setLong(3, bookId);
                            updateStatement.executeUpdate();
                            return true;
                        }
                    } else {
                        sql = "INSERT INTO cartitems(user_id, book_id, amount) VALUES ((SELECT user_id FROM userauths WHERE token = ?), ?, ?)";
                        try (PreparedStatement insertStatement = conn.prepareStatement(sql)) {
                            insertStatement.setLong(1, token);
                            insertStatement.setLong(2, bookId);
                            insertStatement.setLong(3, amount);
                            insertStatement.executeUpdate();
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItem(long token, long bookId) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM cartitems WHERE user_id = (SELECT user_id FROM userauths WHERE token = ?) AND book_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, token);
                statement.setLong(2, bookId);
                int count = statement.executeUpdate();

                if (count > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean purchaseList(long token) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM cartitems WHERE user_id = (SELECT user_id FROM userauths WHERE token = ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, token);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return false;
                    }
                    else {
                        sql = "INSERT INTO orders(userid, createtime) VALUES ((SELECT user_id FROM userauths WHERE token = ?), NOW())";
                        try (PreparedStatement insertOrderStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                            insertOrderStatement.setLong(1, token);
                            insertOrderStatement.executeUpdate();
                            ResultSet generatedKeys = insertOrderStatement.getGeneratedKeys();
                            if (generatedKeys.next()) {
                                long orderId = generatedKeys.getLong(1);

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

                                sql = "DELETE FROM cartitems WHERE user_id = (SELECT user_id FROM userauths WHERE token = ?)";
                                try (PreparedStatement deleteListStatement = conn.prepareStatement(sql)) {
                                    deleteListStatement.setLong(1, token);
                                    deleteListStatement.executeUpdate();
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
