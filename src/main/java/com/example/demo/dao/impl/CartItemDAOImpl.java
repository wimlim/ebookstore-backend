package com.example.demo.dao.impl;

import com.example.demo.dao.CartItemDAO;
import com.example.demo.entity.Book;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.User;
import com.example.demo.repository.CartItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CartItemDAOImpl implements CartItemDAO {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<CartItem> findByUser(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Override
    public CartItem findByUserAndBook(User user, Book book) {
        return cartItemRepository.findByUserAndBook(user, book);
    }

    @Override
    @Transactional
    public void save(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void delete(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {
        cartItemRepository.deleteByUser(user);
    }
}
