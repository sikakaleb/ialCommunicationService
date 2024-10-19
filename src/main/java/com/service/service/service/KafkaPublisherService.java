package com.service.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisherService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Publier un message pour un docteur
    public void sendMessageToDoctor(String doctorId, String message) {
        kafkaTemplate.send("doctor-" + doctorId, message);
    }

    // Publier un message pour une infirmière
    public void sendMessageToNurse(String nurseId, String message) {
        kafkaTemplate.send("nurse-" + nurseId, message);
    }

    // Publier un message pour un patient
    public void sendMessageToPatient(String patientId, String message) {
        kafkaTemplate.send("patient-" + patientId, message);
    }

    // Publier un message pour un proche
    public void sendMessageToRelative(String relativeId, String message) {
        kafkaTemplate.send("relative-" + relativeId, message);
    }
}
