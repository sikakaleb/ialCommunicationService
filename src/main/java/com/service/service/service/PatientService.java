package com.service.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    private KafkaPublisherService kafkaPublisherService;

    public void sendMessage(String patientId, String message) {
        kafkaPublisherService.sendMessageToPatient(patientId, message);
    }
}