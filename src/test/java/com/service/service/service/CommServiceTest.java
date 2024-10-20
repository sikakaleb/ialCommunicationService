package com.service.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class CommServiceTest {

    @Mock
    private DoctorService doctorService;

    @Mock
    private NurseService nurseService;

    @Mock
    private PatientService patientService;

    @Mock
    private RelativeService relativeService;

    @InjectMocks
    private CommService commService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialise les mocks
    }

    @Test
    void testSendMessageToDoctor() {
        // Given
        String userId = "doctor123";
        String message = "Message to doctor";
        String userType = "doctor";

        // When
        commService.sendMessageToUser(userId, message, userType);

        // Then
        verify(doctorService, times(1)).sendMessage(userId, message);
        verifyNoInteractions(nurseService, patientService, relativeService);
    }

    @Test
    void testSendMessageToNurse() {
        // Given
        String userId = "nurse123";
        String message = "Message to nurse";
        String userType = "nurse";

        // When
        commService.sendMessageToUser(userId, message, userType);

        // Then
        verify(nurseService, times(1)).sendMessage(userId, message);
        verifyNoInteractions(doctorService, patientService, relativeService);
    }

    @Test
    void testSendMessageToPatient() {
        // Given
        String userId = "patient123";
        String message = "Message to patient";
        String userType = "patient";

        // When
        commService.sendMessageToUser(userId, message, userType);

        // Then
        verify(patientService, times(1)).sendMessage(userId, message);
        verifyNoInteractions(doctorService, nurseService, relativeService);
    }

    @Test
    void testSendMessageToRelative() {
        // Given
        String userId = "relative123";
        String message = "Message to relative";
        String userType = "relative";

        // When
        commService.sendMessageToUser(userId, message, userType);

        // Then
        verify(relativeService, times(1)).sendMessage(userId, message);
        verifyNoInteractions(doctorService, nurseService, patientService);
    }

    @Test
    void testInvalidUserType() {
        // Given
        String userId = "unknown123";
        String message = "Message to unknown";
        String userType = "unknown";

        // Expect IllegalArgumentException when calling with an invalid userType
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            commService.sendMessageToUser(userId, message, userType);
        });

        // Ensure no service is called
        verifyNoInteractions(doctorService, nurseService, patientService, relativeService);
    }
}
