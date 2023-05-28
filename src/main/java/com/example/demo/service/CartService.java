package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class CartService {

    private static final String URL = "jdbc:mysql://localhost/bookstore?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "sql.14159265";
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    public CartService(CartItemRepository cartItemRepository, BookRepository bookRepository, UserAuthRepository userAuthRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.userAuthRepository = userAuthRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

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
            jsonObject.put("id", cartItem.getBook().getId()); // Replace "id" with the actual identifier field in your CartItem class
            jsonObject.put("title", cartItem.getBook().getTitle());
            jsonObject.put("amount", cartItem.getAmount());
            jsonObject.put("price", cartItem.getBook().getPrice());
            listJson.add(jsonObject);
        }
        return listJson.toJSONString();
    }


    public boolean updateItem(int token, int bookId, int amount) {
        Optional<UserAuth> userAuthOptional = userAuthRepository.findByToken(token);
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (userAuthOptional.isPresent() && bookOptional.isPresent()) {
            UserAuth userAuth = userAuthOptional.get();
            Book book = bookOptional.get();
            Optional<CartItem> cartItemOptional = cartItemRepository.findByUserAndBook(userAuth.getUser(), book);
            if (cartItemOptional.isPresent()) {
                CartItem cartItem = cartItemOptional.get();
                cartItem.setAmount((int) amount);
                cartItemRepository.save(cartItem);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setUser(userAuth.getUser());
                cartItem.setBook(book);
                cartItem.setAmount((int) amount);
                cartItemRepository.save(cartItem);
            }
            return true;
        }
        return false;
    }

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
