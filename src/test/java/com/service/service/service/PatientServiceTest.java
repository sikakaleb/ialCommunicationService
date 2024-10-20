package com.service.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private KafkaPublisherService kafkaPublisherService;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialise les mocks
    }

    @Test
    void testSendMessage() {
        // Given
        String patientId = "patient123";
        String message = "Test message for patient";

        // When
        patientService.sendMessage(patientId, message);

        // Then
        verify(kafkaPublisherService, times(1)).sendMessageToPatient(patientId, message);
        verifyNoMoreInteractions(kafkaPublisherService);
    }
}
