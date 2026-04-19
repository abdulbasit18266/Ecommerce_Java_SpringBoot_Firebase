package com.mobilestore.Abdulbasit.service;

import com.mobilestore.Abdulbasit.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductFirestoreService firestoreService;

    // 1. Get All Products
    public List<Product> getAllProducts() {
        try {
            return firestoreService.getAllProducts();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // 2. Save or Update Product (Fixes 'saveProduct' error)
    public void saveProduct(Product product) {
        try {
            firestoreService.saveProduct(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. Get Single Product by ID (Fixes 'getProductById' error)
    public Product getProductById(String id) {
        try {
            return firestoreService.getProductById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 4. Delete Product (Fixes 'deleteProduct' error)
    public void deleteProduct(String id) {
        try {
            firestoreService.deleteProduct(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProductsByBrand(String brand) {
        try {
            return firestoreService.getProductsByBrand(brand);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}