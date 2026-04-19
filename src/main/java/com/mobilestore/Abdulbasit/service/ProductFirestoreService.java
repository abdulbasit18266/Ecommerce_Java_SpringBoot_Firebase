package com.mobilestore.Abdulbasit.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.mobilestore.Abdulbasit.entity.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ProductFirestoreService {

    private static final String COLLECTION_NAME = "products";

    // 1. Fetch all products (WITH SORTING)
    public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // .orderBy("brand") se devices brand ke hisaab se line se aayenge
        // .orderBy("name") se model name bhi sequence mein rahega
        Query query = db.collection(COLLECTION_NAME).orderBy("brand").orderBy("name");

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        List<Product> productList = new ArrayList<>();

        System.out.println("DEBUG: Connection successful. Total devices: " + documents.size());

        for (QueryDocumentSnapshot document : documents) {
            try {
                Product product = document.toObject(Product.class);
                product.setId(document.getId());
                productList.add(product);
            } catch (Exception e) {
                System.err.println("DEBUG: Error mapping ID " + document.getId() + " - " + e.getMessage());
            }
        }
        return productList;
    }

    // 2. Filter by brand
    public List<Product> getProductsByBrand(String brand) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        // Specific brand ke andar bhi model name sequence mein rahega
        Query query = db.collection(COLLECTION_NAME)
                .whereEqualTo("brand", brand)
                .orderBy("name");

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<Product> productList = new ArrayList<>();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Product product = document.toObject(Product.class);
            product.setId(document.getId());
            productList.add(product);
        }
        return productList;
    }

    // 3. Save or Update
    public String saveProduct(Product product) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        if (product.getId() == null || product.getId().isEmpty()) {
            DocumentReference docRef = db.collection(COLLECTION_NAME).document();
            product.setId(docRef.getId());
            docRef.set(product).get();
            return docRef.getId();
        } else {
            db.collection(COLLECTION_NAME).document(product.getId()).set(product).get();
            return product.getId();
        }
    }

    // 4. Get by ID
    public Product getProductById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot document = db.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            Product product = document.toObject(Product.class);
            product.setId(document.getId());
            return product;
        }
        return null;
    }

    // 5. Delete
    public void deleteProduct(String id) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION_NAME).document(id).delete();
    }
}