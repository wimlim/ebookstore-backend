package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.UserAuthDao;
import com.example.demo.dao.OrderDao;
import com.example.demo.dao.OrderItemDao;
import com.example.demo.dao.BookDao;
import com.example.demo.entity.*;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final UserAuthDao userAuthDao;
    private final OrderItemDao orderItemDao;
    private final BookDao bookDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserAuthDao userAuthDao, OrderItemDao orderItemDao, BookDao bookDao) {
        this.orderDao = orderDao;
        this.userAuthDao = userAuthDao;
        this.orderItemDao = orderItemDao;
        this.bookDao = bookDao;
    }

    @Override
    public JSONArray getOrderItemsWithTimestamps(int token) {
        JSONArray timestampsJson = new JSONArray();
        UserAuth userAuth = userAuthDao.findByToken(token);
        if (userAuth != null) {
            List<Order> orders = orderDao.findByUser(userAuth.getUser());
            Map<Date, List<OrderItem>> orderItemsMap = new TreeMap<>(Collections.reverseOrder());

            for (Order order : orders) {
                List<OrderItem> orderItems = order.getOrderItems();
                for (OrderItem orderItem : orderItems) {
                    Date createTime = order.getCreateTime();
                    if (orderItemsMap.containsKey(createTime)) {
                        orderItemsMap.get(createTime).add(orderItem);
                    } else {
                        List<OrderItem> itemList = new ArrayList<>();
                        itemList.add(orderItem);
                        orderItemsMap.put(createTime, itemList);
                    }
                }
            }

            for (Map.Entry<Date, List<OrderItem>> entry : orderItemsMap.entrySet()) {
                Date createTime = entry.getKey();
                List<OrderItem> itemList = entry.getValue();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                String timestamp = dateFormat.format(createTime);

                JSONObject timestampJson = new JSONObject();
                JSONArray itemListJson = new JSONArray();

                for (OrderItem item : itemList) {
                    JSONObject itemJson = new JSONObject();
                    itemJson.put("bookId", item.getBook().getId());
                    itemJson.put("title", item.getBook().getTitle());
                    itemJson.put("num", item.getNum());
                    itemJson.put("price", item.getBook().getPrice());
                    itemListJson.add(itemJson);
                }

                timestampJson.put("timestamp", timestamp);
                timestampJson.put("items", itemListJson);
                timestampsJson.add(timestampJson);
            }
        }

        return timestampsJson;
    }

    @Override
    public boolean addOrderItem(int token, int bookId) {
        UserAuth userAuth = userAuthDao.findByToken(token);
        if (userAuth != null) {
            Book book = bookDao.findById(bookId);
            if (book == null) {
                return false;
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setBook(book);
            orderItem.setNum(1);

            Order order = new Order();
            order.setUser(userAuth.getUser());
            order.setCreateTime(new Date());
            order.setOrderItems(new ArrayList<>());
            order.getOrderItems().add(orderItem);

            orderItem.setOrder(order);

            orderDao.saveOrder(order);
            orderItemDao.saveOrderItem(orderItem);

            return true;
        } else {
            return false;
        }
    }
}
