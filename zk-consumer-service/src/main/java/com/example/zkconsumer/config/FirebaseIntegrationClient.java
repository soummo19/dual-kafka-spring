package com.example.zkconsumer.config;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component
@RequiredArgsConstructor  
public class FirebaseIntegrationClient {
    private final ResourceLoader resourceLoader;

    @Value("${firebase.firebase-config-file}")
    private String firebaseConfigPath;

    @PostConstruct 
    public void initializeFirebase() {
        try(InputStream credentialStream = resolveCredentialStream()){
            if(credentialStream == null) {
                log.error("Firebase credential file not found at path: {}", firebaseConfigPath);
                return;
            }
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialStream);
            FirebaseOptions.Builder optionsBuilder = FirebaseOptions.builder()
                    .setCredentials(credentials);
            if(FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(optionsBuilder.build());
                log.info("Firebase initialized successfully.");
            } else {
                log.info("Firebase already initialized.");
            }
        } catch (Exception e) {
            log.error("Error initializing Firebase: {}", e.getMessage(), e);
        }
    }

    private InputStream resolveCredentialStream() {
        try {
            if (firebaseConfigPath.startsWith("classpath:")) {
                return resourceLoader.getResource(firebaseConfigPath).getInputStream();
            } else {
                return resourceLoader.getResource("file:" + firebaseConfigPath).getInputStream();
            }
        } catch (Exception e) {
            log.error("Error loading Firebase credential file: {}", e.getMessage(), e);
            return null;
        }
    }
}
