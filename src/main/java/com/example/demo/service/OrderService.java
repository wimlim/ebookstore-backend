package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.*;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserAuthRepository userAuthRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, BookRepository bookRepository, OrderItemRepository orderItemRepository, UserAuthRepository userAuthRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.orderItemRepository = orderItemRepository;
        this.userAuthRepository = userAuthRepository;
    }

    public JSONArray getOrderItemsWithTimestamps(int token) {
        JSONArray timestampsJson = new JSONArray();
        Optional<UserAuth> userAuth = userAuthRepository.findByToken(token);
        if (userAuth.isPresent()) {
            User user = userAuth.get().getUser();
            List<Order> orders = orderRepository.findByUserId(user.getId());
            Map<String, List<OrderItem>> orderItemsMap = new HashMap<>();

            for (Order order : orders) {
                List<OrderItem> orderItems = order.getOrderItems();
                for (OrderItem orderItem : orderItems) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                    String timestamp = dateFormat.format(order.getCreateTime());
                    if (orderItemsMap.containsKey(timestamp)) {
                        orderItemsMap.get(timestamp).add(orderItem);
                    } else {
                        List<OrderItem> itemList = new ArrayList<>();
                        itemList.add(orderItem);
                        orderItemsMap.put(timestamp, itemList);
                    }
                }
            }

            List<Map.Entry<String, List<OrderItem>>> sortedEntries = new ArrayList<>(orderItemsMap.entrySet());
            Collections.sort(sortedEntries, (e1, e2) -> {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                try {
                    Date date1 = dateFormat.parse(e1.getKey());
                    Date date2 = dateFormat.parse(e2.getKey());
                    return date2.compareTo(date1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            });

            for (Map.Entry<String, List<OrderItem>> entry : sortedEntries) {
                String timestamp = entry.getKey();
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
        }

        return timestampsJson;
    }

    public boolean addOrderItem(int token, int bookId) {
        try {
            System.out.println(token);
            Optional<UserAuth> userAuth = userAuthRepository.findByToken(token);
            if (userAuth.isPresent()) {
                User user = userAuth.get().getUser();

                Optional<Book> optionalBook = bookRepository.findById(bookId);
                if (optionalBook.isEmpty()) {
                    return false;
                }
                Book book = optionalBook.get();

                OrderItem orderItem = new OrderItem();
                orderItem.setBook(book);
                orderItem.setNum(1);

                Order order = new Order();
                order.setUserId(user.getId());
                order.setCreateTime(new Date());
                order.setOrderItems(new ArrayList<>());
                order.getOrderItems().add(orderItem);

                orderItem.setOrder(order);

                orderRepository.save(order);
                orderItemRepository.save(orderItem);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
