package com.example.zkconsumer.utils;

import org.springframework.web.bind.annotation.RestController;

import com.example.zkconsumer.model.dto.AndroidFirebaseSendRequest;
import com.example.zkconsumer.service.FirebaseIntegrationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.*;


@Slf4j 
@RestController
@RequestMapping("/utility/v1")
@RequiredArgsConstructor 
public class UtilityController {

    private final FirebaseIntegrationService firebaseIntegrationService;
    
    @PostMapping("/sendToAndroidPushNotification")
    @ResponseStatus(HttpStatus.OK)
    public String sendToAndroidPushNotification(@RequestBody AndroidFirebaseSendRequest request) {
        log.info("Received request to send push notification: {}", ZkConsumerServiceUtils.getJsonPrettyPrint(request));
        Map<String,String>notificationData = new HashMap<>();
        notificationData.put("sender", "zk-consumer");
        firebaseIntegrationService.sendPushNotificationToAndroidDevice(request.getDeviceTokens(), request.getFids(), request.getTitle(), request.getBody(), notificationData);
        return "send";
    }
    
}
