package com.service.service.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {

    @Autowired
    private KafkaPublisherService kafkaPublisherService;

    public void sendMessage(String doctorId, String message) {
        kafkaPublisherService.sendMessageToDoctor(doctorId, message);
    }
}
