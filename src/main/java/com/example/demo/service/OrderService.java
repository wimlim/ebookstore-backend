package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Book;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, BookRepository bookRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.orderItemRepository = orderItemRepository;
    }


    public JSONArray getOrderItemsWithTimestamps(long userId) {
        JSONArray timestampsJson = new JSONArray();
        List<Order> orders = orderRepository.findByUserId(userId);
        Map<Long, List<OrderItem>> orderItemsMap = new HashMap<>();

        for (Order order : orders) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                long timestamp = order.getCreateTime().getTime();
                if (orderItemsMap.containsKey(timestamp)) {
                    orderItemsMap.get(timestamp).add(orderItem);
                } else {
                    List<OrderItem> itemList = new ArrayList<>();
                    itemList.add(orderItem);
                    orderItemsMap.put(timestamp, itemList);
                }
            }
        }

        for (Map.Entry<Long, List<OrderItem>> entry : orderItemsMap.entrySet()) {
            long timestamp = entry.getKey();
            List<OrderItem> itemList = entry.getValue();

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

        return timestampsJson;
    }


    public boolean addOrderItem(Long userId, Long bookId) {
        /*
        try {
            // 获取用户对象
            User user = userRepository.findById(userId);

            // 获取图书对象
            Optional<Book> optionalBook = bookRepository.findById(bookId.intValue());
            if (optionalBook.isEmpty()) {
                // 处理图书不存在的情况
                return false;
            }
            Book book = optionalBook.get();

            // 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setBook(book);
            orderItem.setNum(1); // 设置数量，这里假设默认为1

            // 创建新订单
            Order order = new Order();
            order.setUser(user);
            order.setCreateTime(new Date());
            order.setOrderItems(new ArrayList<>());

            // 添加订单项到订单
            order.getOrderItems().add(orderItem);
            orderItem.setOrder(order);

            // 保存订单和订单项
            orderRepository.save(order);
            orderItemRepository.save(orderItem);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
            */
        return true;
    }


}
