package com.service.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class RelativeServiceTest {

    @Mock
    private KafkaPublisherService kafkaPublisherService;

    @InjectMocks
    private RelativeService relativeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialise les mocks
    }

    @Test
    void testSendMessage() {
        // Given
        String relativeId = "relative123";
        String message = "Test message for relative";

        // When
        relativeService.sendMessage(relativeId, message);

        // Then
        verify(kafkaPublisherService, times(1)).sendMessageToRelative(relativeId, message);
        verifyNoMoreInteractions(kafkaPublisherService);
    }
}
