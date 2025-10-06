package com.example.links.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.links.dto.LinkPreviewRequest;
// import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, LinkPreviewRequest> kafkaTemplate;

    // private final ObjectMapper objectMapper = new ObjectMapper();


    public void sendLinkForPreview(LinkPreviewRequest request) {
        System.out.println("Enviando URL para preview: " + request.getUrl());
        log.debug("Enviando URL para preview: " + request.getUrl());
        kafkaTemplate.send(KafkaConfig.LINK_PREVIEW_TOPIC, request);
    }
}
