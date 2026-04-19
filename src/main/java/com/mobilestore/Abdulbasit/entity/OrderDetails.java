package com.mobilestore.Abdulbasit.entity;

import lombok.Data;

/**
 * Clean POJO class for Firestore.
 * JPA aur MySQL annotations (@Entity, @Table, @Id) hata diye gaye hain.
 */
@Data
public class OrderDetails {

    // ✅ FIXED: Long/int ko badal kar String kar diya Firestore Auto-ID ke liye
    private String id;

    private int userId;
    private String fullName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phoneNumber;

    // Default Constructor (Firestore requirement)
    public OrderDetails() {}
}