package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.*;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserAuthRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public String getList(int token) {
        Optional<UserAuth> userAuthOptional = userAuthRepository.findByToken(token);
        List<CartItem> cartItems = new ArrayList<>();
        if (userAuthOptional.isPresent()) {
            UserAuth userAuth = userAuthOptional.get();
            cartItems = cartItemRepository.findByUser(userAuth.getUser());
        }
        JSONArray listJson = new JSONArray();
        for (CartItem cartItem : cartItems) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", cartItem.getBook().getId());
            jsonObject.put("title", cartItem.getBook().getTitle());
            jsonObject.put("amount", cartItem.getAmount());
            jsonObject.put("price", cartItem.getBook().getPrice());
            listJson.add(jsonObject);
        }
        return listJson.toJSONString();
    }

    @Override
    public boolean updateItem(int token, int bookId, int amount) {
        Optional<UserAuth> userAuthOptional = userAuthRepository.findByToken(token);
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (userAuthOptional.isPresent() && bookOptional.isPresent()) {
            UserAuth userAuth = userAuthOptional.get();
            Book book = bookOptional.get();
            Optional<CartItem> cartItemOptional = cartItemRepository.findByUserAndBook(userAuth.getUser(), book);
            if (cartItemOptional.isPresent()) {
                CartItem cartItem = cartItemOptional.get();
                cartItem.setAmount(amount);
                cartItemRepository.save(cartItem);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setUser(userAuth.getUser());
                cartItem.setBook(book);
                cartItem.setAmount(amount);
                cartItemRepository.save(cartItem);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteItem(int token, int bookId) {
        Optional<UserAuth> userAuthOptional = userAuthRepository.findByToken(token);
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (userAuthOptional.isPresent() && bookOptional.isPresent()) {
            UserAuth userAuth = userAuthOptional.get();
            Book book = bookOptional.get();
            Optional<CartItem> cartItem = cartItemRepository.findByUserAndBook(userAuth.getUser(), book);

            if (cartItem.isPresent()) {
                cartItemRepository.delete(cartItem.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean purchaseList(int token) {
        Optional<UserAuth> userAuthOptional = userAuthRepository.findByToken(token);
        if (userAuthOptional.isPresent()) {
            UserAuth userAuth = userAuthOptional.get();

            List<CartItem> cartItems = cartItemRepository.findByUser(userAuth.getUser());
            if (cartItems.isEmpty()) {
                return false;
            }

            Order order = new Order();
            order.setUser(userAuth.getUser());
            order.setCreateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            orderRepository.save(order);
            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setBook(cartItem.getBook());
                orderItem.setNum(cartItem.getAmount());
                orderItemRepository.save(orderItem);
            }
            cartItemRepository.deleteByUser(userAuth.getUser());
            return true;
        }
        return false;
    }
}
