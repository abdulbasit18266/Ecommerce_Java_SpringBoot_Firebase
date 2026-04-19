package com.mobilestore.Abdulbasit.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.mobilestore.Abdulbasit.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CartService {

    @Autowired
    private ProductFirestoreService productFirestoreService; // CORRECTED: Injected service

    private static final String COLLECTION_NAME = "carts";

    public void addToCart(String userId, String productId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference cartRef = db.collection(COLLECTION_NAME).document(userId)
                .collection("items").document(productId);

        CartItem item = new CartItem(productId, 1);
        cartRef.set(item).get();
    }

    public List<Object[]> getCartWithProducts(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference itemsRef = db.collection(COLLECTION_NAME).document(userId).collection("items");

        ApiFuture<QuerySnapshot> future = itemsRef.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Object[]> cartDetails = new ArrayList<>();

        for (QueryDocumentSnapshot doc : documents) {
            String productId = doc.getString("productId");
            // Product fetch using correct service name
            Product p = productFirestoreService.getProductById(productId);

            if (p != null) {
                // Mapping: 0:Name, 1:Brand, 2:Price, 3:Image, 4:ID
                cartDetails.add(new Object[]{p.getName(), p.getBrand(), p.getPrice(), p.getImage(), p.getId()});
            }
        }
        return cartDetails;
    }

    public void removeFromCart(String userId, String productId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION_NAME).document(userId)
                .collection("items").document(productId).delete().get();
    }

    public static class CartItem {
        private String productId;
        private int quantity;
        public CartItem() {}
        public CartItem(String productId, int quantity) { this.productId = productId; this.quantity = quantity; }
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}