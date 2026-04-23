package com.mobilestore.Abdulbasit.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.mobilestore.Abdulbasit.entity.Order;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    // ✅ Sync: Admin ke liye saare orders fetch karna aur ID set karna
    public List<Order> getAllOrders() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            return db.collection("orders").get().get().getDocuments().stream()
                    .map(doc -> {
                        Order o = doc.toObject(Order.class);
                        o.setId(doc.getId()); // Syncing Document ID
                        return o;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ✅ Sync: Customer ke liye email base par orders fetch karna
    public List<Order> getOrdersByUserEmail(String email) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            return db.collection("orders")
                    .whereEqualTo("email", email)
                    .get().get()
                    .getDocuments().stream()
                    .map(doc -> {
                        Order o = doc.toObject(Order.class);
                        o.setId(doc.getId()); // Syncing Document ID
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

    // ✅ Save Logic: Total Amount controller se aayega, yahan sirf save hoga
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
            // Document reference set karke data save karna
            db.collection("orders").document(order.getId()).set(order).get();
        } catch (Exception e) {
            throw new RuntimeException("Order save failed", e);
        }
    }

    public void updateStatus(String orderId, String status) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("orders").document(orderId).update("status", status).get();
    }
}