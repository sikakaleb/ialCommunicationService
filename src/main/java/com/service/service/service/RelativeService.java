package com.service.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelativeService {

    @Autowired
    private KafkaPublisherService kafkaPublisherService;

    public void sendMessage(String relativeId, String message) {
        kafkaPublisherService.sendMessageToRelative(relativeId, message);
    }
}