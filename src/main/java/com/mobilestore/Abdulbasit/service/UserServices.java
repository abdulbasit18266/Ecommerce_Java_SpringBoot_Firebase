package com.mobilestore.Abdulbasit.service;

import com.mobilestore.Abdulbasit.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    @Autowired
    private UserFirestoreService userFirestoreService;

    public void saveUser(User user) {
        try {
            userFirestoreService.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User login(String email, String password) {
        try {
            // Firestore se user fetch karna
            User user = userFirestoreService.findByEmail(email);

            if (user != null && user.getPassword() != null) {
                if (user.getPassword().equals(password)) {
                    return user; // Success
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Login logic failed");
            e.printStackTrace();
        }
        return null; // Invalid credentials
    }
}