package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.CartItemDAO;
import com.example.demo.dao.BookDao;
import com.example.demo.dao.UserAuthDao;
import com.example.demo.dao.OrderDao;
import com.example.demo.dao.OrderItemDao;
import com.example.demo.entity.*;
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
    private CartItemDAO cartItemDAO;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    public CartServiceImpl(CartItemDAO cartItemDAO, BookDao bookDao, UserAuthDao userAuthDao, OrderDao orderDao, OrderItemDao orderItemDao) {
        this.cartItemDAO = cartItemDAO;
        this.bookDao = bookDao;
        this.userAuthDao = userAuthDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public String getList(int token) {
        UserAuth userAuth = userAuthDao.findByToken(token);
        List<CartItem> cartItems = new ArrayList<>();
        if (userAuth != null) {
            cartItems = cartItemDAO.findByUser(userAuth.getUser());
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
        UserAuth userAuth = userAuthDao.findByToken(token);
        Book book = bookDao.findById(bookId);
        if (userAuth != null && book != null) {
            CartItem cartItem = cartItemDAO.findByUserAndBook(userAuth.getUser(), book);
            if (cartItem != null) {
                cartItem.setAmount(amount);
                cartItemDAO.save(cartItem);
            } else {
                CartItem newcartItem = new CartItem();
                newcartItem.setUser(userAuth.getUser());
                newcartItem.setBook(book);
                newcartItem.setAmount(amount);
                cartItemDAO.save(newcartItem);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteItem(int token, int bookId) {
        UserAuth userAuth = userAuthDao.findByToken(token);
        Book book = bookDao.findById(bookId);

        if (userAuth != null && book != null) {
            CartItem cartItem = cartItemDAO.findByUserAndBook(userAuth.getUser(), book);

            if (cartItem != null) {
                cartItemDAO.delete(cartItem);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean purchaseList(int token) {
        UserAuth userAuth = userAuthDao.findByToken(token);
        if (userAuth != null) {
            List<CartItem> cartItems = cartItemDAO.findByUser(userAuth.getUser());
            if (cartItems.isEmpty()) {
                return false;
            }

            Order order = new Order();
            order.setUser(userAuth.getUser());
            order.setCreateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            orderDao.saveOrder(order);
            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setBook(cartItem.getBook());
                orderItem.setNum(cartItem.getAmount());
                orderItemDao.saveOrderItem(orderItem);
            }
            cartItemDAO.deleteByUser(userAuth.getUser());
            return true;
        }
        return false;
    }
}
