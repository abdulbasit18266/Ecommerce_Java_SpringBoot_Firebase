package com.mobilestore.Abdulbasit.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.mobilestore.Abdulbasit.entity.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ProductFirestoreService {

    private Firestore getDb() {
        return FirestoreClient.getFirestore();
    }

    public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
        Firestore db = getDb();
        ApiFuture<QuerySnapshot> future = db.collection("products")
                .orderBy("brand", Query.Direction.ASCENDING)
                .orderBy("name", Query.Direction.ASCENDING)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Product> products = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Product product = document.toObject(Product.class);
            product.setId(document.getId());
            products.add(product);
        }
        return products;
    }

    public List<Product> getProductsByBrand(String brand) throws ExecutionException, InterruptedException {
        Firestore db = getDb();
        ApiFuture<QuerySnapshot> future = db.collection("products").whereEqualTo("brand", brand).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Product> products = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Product product = document.toObject(Product.class);
            product.setId(document.getId());
            products.add(product);
        }
        return products.stream().sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName())).collect(Collectors.toList());
    }

    public void saveProduct(Product product) throws ExecutionException, InterruptedException {
        Firestore db = getDb();
        if (product.getId() == null || product.getId().isEmpty()) {
            db.collection("products").add(product);
        } else {
            db.collection("products").document(product.getId()).set(product);
        }
    }

    public Product getProductById(String id) throws ExecutionException, InterruptedException {
        Firestore db = getDb();
        DocumentSnapshot doc = db.collection("products").document(id).get().get();
        return doc.exists() ? doc.toObject(Product.class) : null;
    }

    public void deleteProduct(String id) {
        getDb().collection("products").document(id).delete();
    }
}