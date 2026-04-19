package com.mobilestore.Abdulbasit.service;

import com.mobilestore.Abdulbasit.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderFirestoreService orderFirestoreService;

    public List<Order> getAllOrders() {
        try {
            return orderFirestoreService.getAllOrders();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void saveOrder(Order order) {
        try {
            orderFirestoreService.saveOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}