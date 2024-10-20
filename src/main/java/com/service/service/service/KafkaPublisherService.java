package com.service.service.service;

import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaPublisherService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Publier un message pour un docteur avec gestion des ACK
    public void sendMessageToDoctor(String doctorId, String message) {
        sendMessage("doctor-" + doctorId, message);
    }

    // Publier un message pour une infirmière avec gestion des ACK
    public void sendMessageToNurse(String nurseId, String message) {
        sendMessage("nurse-" + nurseId, message);
    }

    // Publier un message pour un patient avec gestion des ACK
    public void sendMessageToPatient(String patientId, String message) {
        sendMessage("patient-" + patientId, message);
    }

    // Publier un message pour un proche avec gestion des ACK
    public void sendMessageToRelative(String relativeId, String message) {
        sendMessage("relative-" + relativeId, message);
    }

    // Méthode réutilisée pour tous les types de messages avec gestion des callbacks et retry
    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 20000)) // Retenter jusqu'à 5 fois avec un délai de 2s entre chaque tentative
    private void sendMessage(String topic, String message) {
        // Envoi du message avec KafkaTemplate et récupération du résultat dans un ListenableFuture
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.thenAccept(result -> {
            System.out.println("Message envoyé avec succès");
        }).exceptionally(ex -> {
            System.err.println("Erreur lors de l'envoi: " + ex.getMessage());
            retrySendMessage(topic, message); // réessayer en cas d'échec
            return null;
        });
    }

    // Méthode pour réessayer l'envoi du message en cas d'échec
    private void retrySendMessage(String topic, String message) {
        // Logique de réessai en cas d'erreur
        System.out.println("Réessai d'envoi du message sur le topic " + topic);
        sendMessage(topic, message); // Réessai d'envoi du message
    }
}