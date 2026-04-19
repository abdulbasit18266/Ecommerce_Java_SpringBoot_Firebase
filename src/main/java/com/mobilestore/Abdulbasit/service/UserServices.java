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
            User user = userFirestoreService.findByEmail(email);
            // Plain text comparison for now (since register is also plain text)
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}