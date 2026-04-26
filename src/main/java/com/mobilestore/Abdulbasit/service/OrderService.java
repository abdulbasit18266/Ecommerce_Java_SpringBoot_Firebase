package com.mobilestore.Abdulbasit.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.mobilestore.Abdulbasit.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private MailService mailService; //

    // ✅ Admin ke liye saare orders fetch karna
    public List<Order> getAllOrders() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            return db.collection("orders").get().get().getDocuments().stream()
                    .map(doc -> {
                        Order o = doc.toObject(Order.class);
                        o.setId(doc.getId());
                        return o;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ✅ Customer ke liye email base par orders fetch karna
    public List<Order> getOrdersByUserEmail(String email) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            return db.collection("orders")
                    .whereEqualTo("email", email)
                    .get().get()
                    .getDocuments().stream()
                    .map(doc -> {
                        Order o = doc.toObject(Order.class);
                        o.setId(doc.getId());
                        return o;
                    })
                    .sorted((o1, o2) -> {
                        if (o1.getOrderDate() == null || o2.getOrderDate() == null) return 0;
                        return o2.getOrderDate().compareTo(o1.getOrderDate());
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ✅ Save Logic with Automatic Email Trigger
    public void saveOrder(Order order) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            if (order.getId() == null || order.getId().isEmpty()) {
                order.setId(UUID.randomUUID().toString());
            }
            if (order.getStatus() == null) order.setStatus("PLACED");
            if (order.getOrderDate() == null) {
                order.setOrderDate(java.time.LocalDateTime.now().toString());
            }

            // 1. Database mein order save karna
            db.collection("orders").document(order.getId()).set(order).get();

            // 2. ✅ Email Bhejna
            // Note: Chunki Order entity mein image field nahi hai,
            // isliye hum yahan ek placeholder icon bhej rahe hain.
            mailService.sendOrderEmail(
                    order.getEmail(),               // Recipient Email
                    order.getCustomerName(),        // Customer Name
                    order.getName(),                // Device Name (e.g. iPhone 13)
                    "https://cdn-icons-png.flaticon.com/512/644/644458.png", // Default Mobile Icon
                    String.valueOf(order.getTotalAmount()),
                    "1",                            // Quantity default
                    order.getAddress()              // Delivery Address
            );

        } catch (Exception e) {
            throw new RuntimeException("Order save failed", e);
        }
    }

    public void updateStatus(String orderId, String status) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("orders").document(orderId).update("status", status).get();
    }
}