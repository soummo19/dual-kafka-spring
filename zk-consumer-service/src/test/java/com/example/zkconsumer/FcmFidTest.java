package com.example.zkconsumer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.SendResponse;
import org.junit.jupiter.api.Test;
import java.io.FileInputStream;
import java.util.List;

public class FcmFidTest {
    @Test
    public void testSendViaFid() throws Exception {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/notification-sample-aeaf5-firebase-adminsdk-fbsvc-acbc61c820.json");

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            // Use ONLY FID — no tokens
            String fid = "eH0rqxrYQduj_fQMKrXmRG";

            MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder().setTitle("FID Test").setBody("Sent via FID!").build())
                .putData("title", "FID Test")
                .putData("body", "Sent via FID!")
                .addAllFids(List.of(fid))
                .build();

            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);

            System.out.println("===== FID TEST RESULT =====");
            System.out.println("Success count: " + response.getSuccessCount());
            System.out.println("Failure count: " + response.getFailureCount());

            List<SendResponse> responses = response.getResponses();
            for (int i = 0; i < responses.size(); i++) {
                if (responses.get(i).isSuccessful()) {
                    System.out.println("Message " + i + " sent successfully: " + responses.get(i).getMessageId());
                } else {
                    System.out.println("Message " + i + " FAILED: " + responses.get(i).getException().getMessage());
                    System.out.println("Error code: " + responses.get(i).getException().getMessagingErrorCode());
                }
            }
            System.out.println("===========================");
        } catch (Exception e) {
            System.out.println("===== FID TEST ERROR =====");
            e.printStackTrace();
            System.out.println("==========================");
            throw e;
        }
    }
}
