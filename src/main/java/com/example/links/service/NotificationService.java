package com.example.links.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.links.dto.NotificationDto;

@Service
public class NotificationService {
    
    private final List<NotificationDto> notifications = new ArrayList<>();
    private final int MAX_NOTIFICATIONS = 50;

    public void addNotification(NotificationDto notificationDto) {
        notifications.add(0, notificationDto);

        if (notifications.size() > MAX_NOTIFICATIONS) {
            notifications.remove(notifications.size() - 1);
        }
    }

    public List<NotificationDto> getNotifications(){
        return new ArrayList<>(notifications);
    }

    public void clearNotifications(){
        notifications.clear();
    }
}
