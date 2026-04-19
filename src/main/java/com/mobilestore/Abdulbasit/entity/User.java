package com.mobilestore.Abdulbasit.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Clean POJO class for Firestore.
 * JPA annotations poori tarah hata diye gaye hain.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // Firebase Auto-ID (String)
    private String id;

    private String name;
    private String email;
    private String password;
    private String role; // e.g., "ADMIN" or "USER"
    private String phoneNumber;

    // Empty constructor (Lombok @NoArgsConstructor isse handle kar leta hai)
}