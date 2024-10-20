package com.service.service.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class KafkaPublisherServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaPublisherService kafkaPublisherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMessageToDoctor() {
        // Mock KafkaTemplate to return a completed future
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(mockSendResult());
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);

        // When
        kafkaPublisherService.sendMessageToDoctor("doctorId", "message");

        // Then
        verify(kafkaTemplate, times(1)).send("doctor-doctorId", "message");
    }

    @Test
    void testSendMessageToNurse() {
        // Mock KafkaTemplate to return a completed future
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(mockSendResult());
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);

        // When
        kafkaPublisherService.sendMessageToNurse("nurseId", "message");

        // Then
        verify(kafkaTemplate, times(1)).send("nurse-nurseId", "message");
    }

    @Test
    void testSendMessageToPatient() {
        // Mock KafkaTemplate to return a completed future
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(mockSendResult());
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);

        // When
        kafkaPublisherService.sendMessageToPatient("patientId", "message");

        // Then
        verify(kafkaTemplate, times(1)).send("patient-patientId", "message");
    }

    @Test
    void testSendMessageToRelative() {
        // Mock KafkaTemplate to return a completed future
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(mockSendResult());
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);

        // When
        kafkaPublisherService.sendMessageToRelative("relativeId", "message");

        // Then
        verify(kafkaTemplate, times(1)).send("relative-relativeId", "message");
    }

    // Helper method to create a mock SendResult
    /*private SendResult<String, String> mockSendResult() {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic", "key", "value");
        RecordMetadata recordMetadata = new RecordMetadata(null, 0, 0, 0, 0, 0);
        return new SendResult<>(producerRecord, recordMetadata);
    }*/

    @Test
    void testSendMessageToDoctorSuccess() {
        // Mock KafkaTemplate to return a completed future
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(mockSendResult());
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);

        // When
        kafkaPublisherService.sendMessageToDoctor("doctorId", "message");

        // Then
        verify(kafkaTemplate, times(1)).send("doctor-doctorId", "message");
    }

    @Test
    void testSendMessageToDoctorFailureAndRetry() {
        // Simulate a failure during the first send
        CompletableFuture<SendResult<String, String>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Simulated send failure"));
        when(kafkaTemplate.send(anyString(), anyString()))
                .thenReturn(failedFuture) // First call fails
                .thenReturn(CompletableFuture.completedFuture(mockSendResult())); // Second call succeeds

        // When
        kafkaPublisherService.sendMessageToDoctor("doctorId", "message");

        // Then
        verify(kafkaTemplate, times(2)).send("doctor-doctorId", "message"); // Should be called twice, one for retry
    }

    // Helper method to create a mock SendResult
    private SendResult<String, String> mockSendResult() {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic", "key", "value");
        RecordMetadata recordMetadata = new RecordMetadata(null, 0, 0, 0, 0, 0);
        return new SendResult<>(producerRecord, recordMetadata);
    }
}
