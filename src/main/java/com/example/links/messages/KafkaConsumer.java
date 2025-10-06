package com.example.links.messages;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.links.service.NotificationService;
import com.example.links.dto.LinkPreviewRequest;
import com.example.links.dto.NotificationDto;
import com.example.links.dto.NotificationStatusEnum;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class KafkaConsumer {

    @Autowired
    private NotificationService notificationService;
    
    @KafkaListener(topics = KafkaConfig.LINK_PREVIEW_TOPIC, groupId = KafkaConfig.LINK_PREVIEW_GROUP)
    public void consumeLinkPreviewRequest(LinkPreviewRequest request) {
        System.out.println("Mensagem recebida do kafka: ");
        System.out.println("URL: " + request.getUrl());
        System.out.println("UserId: " + request.getUserId());
        System.out.println("Requested at: " + request.getRequestedAt());
        
        log.debug("Mensagem recebida do kafka: ");
        log.debug("URL: " + request.getUrl());
        log.debug("UserId: " + request.getUserId());
        log.debug("Requested at: " + request.getRequestedAt());

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setMessage("Foi feito pedido para salvar ");
        notificationDto.setStatus(NotificationStatusEnum.INFO);
        notificationDto.setTimestamp(LocalDateTime.now());
        notificationDto.setUrl(request.getUrl());
        notificationService.addNotification(notificationDto);

    }
}
