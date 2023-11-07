package com.example.demo.listener;

import com.example.demo.service.CartService;
import com.example.demo.websocket.WebSocketServer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    @Autowired
    private CartService cartService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String ORDER_MESSAGE_TOPIC = "order-message-topic";
    private static final String ORDER_RESULT_TOPIC = "order-result-topic";
    @Autowired
    private WebSocketServer webSocketServer;
    @KafkaListener(topics = ORDER_MESSAGE_TOPIC, groupId = "order-consumer-group")
    public void listen(ConsumerRecord<String, String> record) {
        String value = record.value();
        int orderToken = Integer.parseInt(value);
        System.out.println("Get message from kafka: " + orderToken);

        // 处理订单
        cartService.purchaseList(orderToken);

        // 构建包含ID和Result的JSON消息
        String result = "{\"id\": " + orderToken + ", \"result\": \"success\"}";
        System.out.println(result);
        webSocketServer.sendMessageToUser(value, result);
    }
}
