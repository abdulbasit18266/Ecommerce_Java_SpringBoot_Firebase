package com.mobilestore.Abdulbasit.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.mobilestore.Abdulbasit.entity.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class OrderFirestoreService {

    private static final String COLLECTION_NAME = "orders";

    // 1. Saare orders fetch karne ke liye
    public List<Order> getAllOrders() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        List<Order> orderList = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Order order = document.toObject(Order.class);
            order.setId(document.getId()); // Firebase ID set kar rahe hain
            orderList.add(order);
        }
        return orderList;
    }

    // 2. Naya order save karne ke liye
    public void saveOrder(Order order) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document();
        order.setId(docRef.getId());
        docRef.set(order);
    }
}