package com.service.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class NurseServiceTest {

    @Mock
    private KafkaPublisherService kafkaPublisherService;

    @InjectMocks
    private NurseService nurseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialise les mocks
    }

    @Test
    void testSendMessage() {
        // Given
        String nurseId = "nurse123";
        String message = "Test message for nurse";

        // When
        nurseService.sendMessage(nurseId, message);

        // Then
        verify(kafkaPublisherService, times(1)).sendMessageToNurse(nurseId, message);
        verifyNoMoreInteractions(kafkaPublisherService);
    }
}
