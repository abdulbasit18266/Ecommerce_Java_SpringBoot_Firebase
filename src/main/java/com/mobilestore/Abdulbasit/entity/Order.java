package com.mobilestore.Abdulbasit.entity;

/**
 * Clean POJO class for Firebase Firestore.
 * Saare JPA/MySQL annotations hata diye gaye hain.
 */
public class Order {

    // ✅ FIXED: int ko badal kar String kar diya
    private String id;

    private String customerName;
    private String email;
    private String address;
    private String phoneNumber;
    private double totalAmount;
    private String orderDate;
    private String status;

    // Default Constructor (Firestore ke liye zaroori hai)
    public Order() {}

    // --- GETTERS & SETTERS ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}