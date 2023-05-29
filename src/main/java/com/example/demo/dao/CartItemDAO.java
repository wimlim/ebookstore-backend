package com.example.demo.dao;

import com.example.demo.entity.Book;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.User;

import java.util.List;

public interface CartItemDAO {
    List<CartItem> findByUser(User user);

    CartItem findByUserAndBook(User user, Book book);

    void save(CartItem cartItem);

    void delete(CartItem cartItem);

    void deleteByUser(User user);
}
