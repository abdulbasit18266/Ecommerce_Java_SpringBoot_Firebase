package com.mobilestore.Abdulbasit; // Tera package name check kar lena sahi hai na

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            // Render ke Environment Variables se data uthana
            String projectId = System.getenv("FIREBASE_PROJECT_ID");
            String clientEmail = System.getenv("FIREBASE_CLIENT_EMAIL");
            String privateKey = System.getenv("FIREBASE_PRIVATE_KEY").replace("\\n", "\n");

            // JSON string manually banana bina file ke
            String jsonConfig = String.format(
                    "{\"type\": \"service_account\", \"project_id\": \"%s\", \"private_key\": \"%s\", \"client_email\": \"%s\"}",
                    projectId, privateKey, clientEmail
            );

            InputStream serviceAccount = new ByteArrayInputStream(jsonConfig.getBytes());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("Firebase initialized successfully!");

        } catch (Exception e) {
            System.err.println("Firebase initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}