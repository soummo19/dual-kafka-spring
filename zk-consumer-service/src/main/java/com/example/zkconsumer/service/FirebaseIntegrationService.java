package com.example.zkconsumer.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.zkconsumer.utils.ZkConsumerServiceUtils;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FcmOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.AndroidConfig.Priority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service 
@RequiredArgsConstructor 
public class FirebaseIntegrationService {
    

    public void sendPushNotificationToAndroidDevice(List<String>deviceTokens, List<String>fids, String title, String body, Map<String,String>notificationData){
        log.info("deviceTokens: {}, fids: {}, title: {}, body: {}", deviceTokens, fids, title, body);
        try{
            MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .putAllData(notificationData)
                // .addAllTokens(deviceTokens)
                .addAllFids(fids)
                .setAndroidConfig(AndroidConfig.builder()
                    .setPriority(Priority.HIGH)
                    .build())
                .setFcmOptions(FcmOptions.builder()
                .setAnalyticsLabel("zk-consumer-service") 
                .build())
                .build(); 
            log.info("payload={}", ZkConsumerServiceUtils.getJsonPrettyPrint(message));
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            handleResponse(response, deviceTokens);
        }
        catch(FirebaseMessagingException e){
            log.error("Error sending push notification: {}", e.getMessage());
        }
        
        
    }

    private void handleResponse(BatchResponse response, List<String> deviceTokens) {
        log.info("Successfully sent messages: {}", response.getSuccessCount());
        log.info("Failed to send messages: {}", response.getFailureCount());
        if (response.getFailureCount() > 0) {
            List<com.google.firebase.messaging.SendResponse> responses = response.getResponses();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    // String failedToken = deviceTokens.get(i);
                    String errorMsg = responses.get(i).getException().getMessage();
                    log.error("Error sending message to token {}", errorMsg);
                }
            }
        }
    }
}
