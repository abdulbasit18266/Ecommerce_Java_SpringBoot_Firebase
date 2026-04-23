package com.mobilestore.Abdulbasit.service;

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
    private ProductFirestoreService productFirestoreService;
    private static final String COLLECTION_NAME = "carts";

    // ✅ Naya Method: Order ke baad cart khali karne ke liye
    public void clearCart(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference items = db.collection(COLLECTION_NAME).document(userId).collection("items");
        List<QueryDocumentSnapshot> documents = items.get().get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            doc.getReference().delete();
        }
    }

    public void addToCart(String userId, String productId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference itemRef = db.collection(COLLECTION_NAME).document(userId).collection("items").document(productId);
        DocumentSnapshot snap = itemRef.get().get();

        if (snap.exists()) {
            long currentQty = snap.getLong("quantity");
            itemRef.update("quantity", currentQty + 1).get();
        } else {
            itemRef.set(new CartItem(productId, 1)).get();
        }
    }

    public void updateQuantity(String userId, String productId, int change) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference itemRef = db.collection(COLLECTION_NAME).document(userId).collection("items").document(productId);
        DocumentSnapshot snap = itemRef.get().get();
        if (snap.exists()) {
            long newQty = snap.getLong("quantity") + change;
            if (newQty > 0) itemRef.update("quantity", newQty).get();
            else removeFromCart(userId, productId);
        }
    }

    public List<Object[]> getCartWithProducts(String userId) throws ExecutionException, InterruptedException {
        if (userId == null || userId.isEmpty()) return new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        List<QueryDocumentSnapshot> docs = db.collection(COLLECTION_NAME).document(userId).collection("items").get().get().getDocuments();
        List<Object[]> cartDetails = new ArrayList<>();

        for (QueryDocumentSnapshot doc : docs) {
            String pId = doc.getString("productId");
            int qty = doc.getLong("quantity").intValue();
            Product p = productFirestoreService.getProductById(pId);
            if (p != null) {
                cartDetails.add(new Object[]{pId, p.getName(), p.getBrand(), p.getPrice(), p.getImage(), qty});
            }
        }
        return cartDetails;
    }

    public void removeFromCart(String userId, String productId) throws ExecutionException, InterruptedException {
        FirestoreClient.getFirestore().collection(COLLECTION_NAME).document(userId).collection("items").document(productId).delete().get();
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