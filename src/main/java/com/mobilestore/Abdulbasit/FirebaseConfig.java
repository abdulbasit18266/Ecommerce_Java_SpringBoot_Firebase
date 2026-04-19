package com.mobilestore.Abdulbasit;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct; // Check karo ye 'jakarta' hi hona chahiye
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            // Path check: file exact yahi honi chahiye
            // FirebaseConfig.java ke andar
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("--------------------------------------------");
            System.out.println("🔥🔥 FIREBASE CONNECTION SUCCESSFUL! 🔥🔥");
            System.out.println("--------------------------------------------");

        } catch (IOException e) {
            System.out.println("❌ FIREBASE ERROR: " + e.getMessage());
        }
    }
}