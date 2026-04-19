package com.mobilestore.Abdulbasit.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.mobilestore.Abdulbasit.entity.Cart;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CartFirestoreService {

    private static final String COLLECTION_NAME = "cart";

    // 1. User ki ID ke hisaab se cart items lana
    public List<Cart> getCartByUserId(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query query = db.collection(COLLECTION_NAME).whereEqualTo("userId", userId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<Cart> cartList = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Cart cart = document.toObject(Cart.class);
            cart.setId(document.getId());
            cartList.add(cart);
        }
        return cartList;
    }

    // 2. Cart mein item save ya update karna
    public void saveToCart(Cart cart) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        if (cart.getId() == null || cart.getId().isEmpty()) {
            DocumentReference docRef = db.collection(COLLECTION_NAME).document();
            cart.setId(docRef.getId());
            docRef.set(cart);
        } else {
            db.collection(COLLECTION_NAME).document(cart.getId()).set(cart);
        }
    }

    // 3. Cart se item delete karna (User checkout ke baad)
    public void deleteCartByUserId(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query query = db.collection(COLLECTION_NAME).whereEqualTo("userId", userId);
        ApiFuture<QuerySnapshot> snapshot = query.get();
        for (DocumentSnapshot doc : snapshot.get().getDocuments()) {
            doc.getReference().delete();
        }
    }
}