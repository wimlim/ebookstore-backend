package com.example.demo.service;

public interface CartService {
    String getList(int token);
    boolean updateItem(int token, int bookId, int amount);
    boolean deleteItem(int token, int bookId);
    boolean purchaseList(int token);
}
