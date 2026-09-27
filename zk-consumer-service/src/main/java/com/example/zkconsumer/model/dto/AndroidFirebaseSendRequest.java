package com.example.zkconsumer.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AndroidFirebaseSendRequest {
    private String title;
    private String body;
    private List<String> deviceTokens;
    private List<String> fids;
}
