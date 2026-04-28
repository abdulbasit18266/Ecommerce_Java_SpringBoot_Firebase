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

### **1. System Requirements**
* **Java JDK:** Version 17 or higher must be installed.
* **Internet Connectivity:** Required for Firebase sync and Email services.
* **System Clock Sync:** Ensure your PC time is set to **"Set time automatically"**. (Mandatory for Firebase Auth).


### **2. Pre-Run Configuration**
* **Firebase Credentials:** Place your `serviceAccountKey.json` file inside the `src/main/resources/` directory.
* **SMTP Setup:** Verify SMTP credentials in `application.properties` for the Email module.


## 🚀 3. EXECUTION STEPS VIA COMMAND PROMPT (CMD)

### Step 1: Open Project Directory
Navigate to the root folder of the project (where `pom.xml` is located). Type `cmd` in the Windows Explorer address bar and press Enter.

### Step 2: Clean and Build Project
Execute the following command to download dependencies and compile the source code:

### cmd command 
'mvnw clean install'.

### Step 3: Run Application
'mvnw spring-boot:run'.


---

## 🔐 Application Access

- Open browser:'http://localhost:8080'.


### Features
- OTP-based password recovery
- Role-based Admin panel
- Order confirmation email system

---

## 🛑 Troubleshooting

- **Port 8080 busy:** Close IntelliJ or other running instances
- **Stop server:** Press `Ctrl + C` then `Y`.

---

## 📁 Project Structure

```text
src/main/java/com/mobilestore/Abdulbasit/
├── controller/       # Web request handlers (Admin, User, Product, Order)
├── entity/           # Data models (User, Product, Order, Cart)
├── service/          # Business logic (MailService, FirebaseService)
└── config/           # Firebase & App configurations

