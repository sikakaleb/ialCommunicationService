package com.service.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NurseService {

    @Autowired
    private KafkaPublisherService kafkaPublisherService;

    public void sendMessage(String nurseId, String message) {
        kafkaPublisherService.sendMessageToNurse(nurseId, message);
    }
}
