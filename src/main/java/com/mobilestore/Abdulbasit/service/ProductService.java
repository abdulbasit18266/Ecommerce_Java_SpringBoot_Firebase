package com.mobilestore.Abdulbasit.service;

import com.mobilestore.Abdulbasit.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ProductService {

    @Autowired
    private ProductFirestoreService firestoreService;

    public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
        return firestoreService.getAllProducts();
    }

    public List<Product> getProductsByBrand(String brand) throws ExecutionException, InterruptedException {
        return firestoreService.getProductsByBrand(brand);
    }

    public void saveProduct(Product product) {
        try {
            firestoreService.saveProduct(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Product getProductById(String id) throws ExecutionException, InterruptedException {
        return firestoreService.getProductById(id);
    }

    public void deleteProduct(String id) {
        firestoreService.deleteProduct(id);
    }
}