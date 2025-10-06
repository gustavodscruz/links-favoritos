package com.example.links.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotificationDto {
    private String message;
    private NotificationStatusEnum status;
    private LocalDateTime timestamp;
    private String url;  
}
