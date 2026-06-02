package com.inventorilab.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "app.firebase.enabled", havingValue = "true")
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            log.info("Initializing Firebase Admin SDK...");

            String path = "src/main/resources/firebase-service-account.json";
            FileInputStream serviceAccount = new FileInputStream(path);

            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase Admin SDK initialized successfully");
            } else {
                log.info("Firebase Admin SDK already initialized");
            }

        } catch (IOException e) {
            log.warn("Firebase Admin SDK tidak dapat diinisialisasi: {}", e.getMessage());
            log.warn("Auth Google/Phone tidak tersedia. Gunakan login email/password.");
        }
    }
}
