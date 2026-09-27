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

public class FcmTokenTest {
    @Test
    public void testSendViaToken() throws Exception {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/notification-sample-aeaf5-firebase-adminsdk-fbsvc-acbc61c820.json");

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            String token = "e8Ok7LGQRUuke6jELucj0T:APA91bE_kM5cPwR4ARSqJLh7StzadC5B9hPE-DN4dXZZdTNHWf3xWx3RwZ8Sbs665xcJT6Ilm2aq9jYope95IfzSN5jEnJB0ppZD14cGLtCRSx9P4zuGL34";

            MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder().setTitle("Token Test").setBody("Sent via Token!").build())
                .putData("title", "Token Test")
                .putData("body", "Sent via Token!")
                .addAllTokens(List.of(token))
                .build();

            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);

            System.out.println("===== TOKEN TEST RESULT =====");
            System.out.println("Success count: " + response.getSuccessCount());
            System.out.println("Failure count: " + response.getFailureCount());

            List<SendResponse> responses = response.getResponses();
            for (int i = 0; i < responses.size(); i++) {
                if (responses.get(i).isSuccessful()) {
                    System.out.println("Message " + i + " sent successfully: " + responses.get(i).getMessageId());
                } else {
                    System.out.println("Message " + i + " FAILED: " + responses.get(i).getException().getMessage());
                }
            }
            System.out.println("===========================");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
