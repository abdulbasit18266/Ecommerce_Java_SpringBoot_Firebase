# Ecommerce_Java_SpringBoot_Firebase
# 📱 Mobile Store - Premium E-Commerce Application

A professional, full-stack E-commerce web application developed as a **Final Year BCA Project**. This platform specializes in premium mobile devices, featuring a sleek "Quiet Luxury" design and robust backend integration.

## 🚀 Key Features

### 👤 User Side
- **User Authentication:** Secure Login and Registration system.
- **Role-Based Access:** Automatic redirection based on user role (Admin/User).
- **Cart Management:** Add products to cart (even before login!), with automatic migration to database after login.
- **Order System:** Seamless checkout process with real-time Firestore database synchronization.
- **Email Notifications:** - Automated **OTP** for Forgot Password.
  - Professional **HTML Order Confirmation Emails** with product details.

### 🛠 Admin Dashboard
- **Real-time Analytics:** Total Products, Total Orders, and Revenue tracking.
- **Inventory Management:** CRUD operations for mobile devices (Apple, Samsung, etc.).
- **Order Management:** View all customer orders and update shipping status (PLACED, SHIPPED, DELIVERED).
- **Data Visualization:** Brand split charts and revenue graphs.

## 💻 Tech Stack

- **Backend:** Java 17, Spring Boot 3.x
- **Database:** Google Firebase (Cloud Firestore)
- **Frontend:** Thymeleaf Template Engine, HTML5, CSS3 (Custom Ivory Coast Theme), JavaScript
- **Security:** Session-based Authentication & Role-Based Access Control (RBAC)
- **Mailing:** Spring Mail Starter (SMTP)
- **Build Tool:** Maven

## 📁 Project Structure

```text
src/main/java/com/mobilestore/Abdulbasit/
├── controller/       # Web request handlers (Admin, User, Product, Order)
├── entity/           # Data models (User, Product, Order, Cart)
├── service/          # Business logic (MailService, FirebaseService)
└── config/           # Firebase & App configurations
