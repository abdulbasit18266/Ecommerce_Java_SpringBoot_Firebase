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

    // ProductFirestoreService.java ke andar is method ko update karein
    public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
        Firestore db = getDb();
        // Brand ke hisaab se sort karke data uthayenge
        ApiFuture<QuerySnapshot> future = db.collection("products")
                .orderBy("brand", Query.Direction.ASCENDING)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Product> products = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Product product = document.toObject(Product.class);
            if (product != null) {
                product.setId(document.getId());
                products.add(product);
            }
        }
        return products;
    }

    // Is method ka naam aur logic check karein (Line 19 error fix karne ke liye)
    public List<Product> getProductsByBrand(String brand) throws ExecutionException, InterruptedException {
        Firestore db = getDb();
        ApiFuture<QuerySnapshot> future = db.collection("products").whereEqualTo("brand", brand).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Product> products = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            Product product = document.toObject(Product.class);
            if (product != null) {
                product.setId(document.getId());
                products.add(product);
            }
        }
        return products;
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
        if (id == null || id.isEmpty()) return null;
        DocumentSnapshot doc = getDb().collection("products").document(id).get().get();
        if (doc.exists()) {
            Product p = doc.toObject(Product.class);
            if (p != null) p.setId(doc.getId());
            return p;
        }
        return null;
    }

    public void deleteProduct(String id) {
        if (id != null && !id.isEmpty()) {
            getDb().collection("products").document(id).delete();
        }
    }
}