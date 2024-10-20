package com.service.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class DoctorServiceTest {

    @Mock
    private KafkaPublisherService kafkaPublisherService;

    @InjectMocks
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialise les mocks
    }

    @Test
    void testSendMessage() {
        // Given
        String doctorId = "doctor123";
        String message = "Test message";

        // When
        doctorService.sendMessage(doctorId, message);

        // Then
        verify(kafkaPublisherService, times(1)).sendMessageToDoctor(doctorId, message);
        verifyNoMoreInteractions(kafkaPublisherService);
    }
}
