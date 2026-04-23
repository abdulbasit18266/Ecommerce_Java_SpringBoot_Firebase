package com.mobilestore.Abdulbasit.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.mobilestore.Abdulbasit.entity.User;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;

@Service
public class UserServices {

    private static final String COLLECTION_NAME = "users";

    public void saveUser(User user) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            // Agar user ki ID null hai, toh email ko hi ID bana dete hain
            if (user.getId() == null || user.getId().isEmpty()) {
                user.setId(user.getEmail());
            }
            db.collection(COLLECTION_NAME).document(user.getEmail()).set(user).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public User login(String email, String password) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            // Querying by email and password
            QuerySnapshot query = db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password)
                    .get().get();

            if (!query.isEmpty()) {
                QueryDocumentSnapshot document = query.getDocuments().get(0);
                User user = document.toObject(User.class);

                // Safety: Agar object ke andar 'id' null hai, toh document ID se bhar do
                if (user != null && (user.getId() == null || user.getId().isEmpty())) {
                    user.setId(document.getId());
                }
                return user;
            }
        } catch (Exception e) {
            System.err.println("Login Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}