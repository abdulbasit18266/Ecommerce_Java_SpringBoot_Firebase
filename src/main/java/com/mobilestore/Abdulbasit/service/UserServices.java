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
            QuerySnapshot query = db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password)
                    .get().get();

            if (!query.isEmpty()) {
                QueryDocumentSnapshot document = query.getDocuments().get(0);
                User user = document.toObject(User.class);
                if (user != null && (user.getId() == null || user.getId().isEmpty())) {
                    user.setId(document.getId());
                }
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ NEW: Find user by email (Forgot password ke liye)
    public User findByEmail(String email) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            QuerySnapshot query = db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", email)
                    .get().get();

            if (!query.isEmpty()) {
                return query.getDocuments().get(0).toObject(User.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ NEW: Update only password in Firestore
    public void updatePassword(String email, String newPassword) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION_NAME).document(email)
                    .update("password", newPassword).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}