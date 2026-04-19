package com.mobilestore.Abdulbasit.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Clean POJO class for Firestore.
 * JPA aur MySQL annotations (@Entity, @Table) poori tarah hata diye gaye hain.
 */
@Data
@NoArgsConstructor // Isse default constructor (Cart()) khud ban jayega
@AllArgsConstructor // Isse saare fields wala constructor ban jayega
public class Cart {

    // Firestore Document ID (e.g., "wqhdfzN0o...")
    private String id;

    // User ki Firebase ID (String)
    private String userId;

    // Product ki Firebase ID (String)
    private String productId;

    // Quantity (Number)
    private int quantity;

    /* Note: @Data annotation se Getters aur Setters
       IntelliJ background mein khud generate kar leta hai.
       Agar aapko errors dikhein, toh IntelliJ mein 'Lombok' plugin install karein.
    */
}