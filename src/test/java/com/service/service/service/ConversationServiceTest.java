package com.service.service.service;


import com.service.service.model.Conversation;
import com.service.service.repository.ConversationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConversationServiceTest {

    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private ConversationService conversationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartConversation() {
        // Arrange
        String initiatorId = "user1";
        List<String> participantIds = Arrays.asList("user2", "user3");

        Conversation conversation = new Conversation();
        conversation.setInitiatorId(initiatorId);
        conversation.setParticipantIds(Arrays.asList(initiatorId, "user2", "user3"));
        conversation.setStartedAt(LocalDateTime.now());
        conversation.setLastUpdated(LocalDateTime.now());
        conversation.setClosed(false);
        conversation.setArchived(false);

        when(conversationRepository.save(any(Conversation.class))).thenReturn(conversation);

        // Act
        Conversation result = conversationService.startConversation(initiatorId, participantIds);

        // Assert
        assertNotNull(result);
        assertEquals(initiatorId, result.getInitiatorId());
        assertTrue(result.getParticipantIds().containsAll(Arrays.asList(initiatorId, "user2", "user3")));
        verify(conversationRepository, times(1)).save(any(Conversation.class));
    }

    @Test
    void testsStartConversation() {
        // Arrange
        String initiatorId = "user1";
        List<String> participantIds = Arrays.asList("user2", "user3");

        Conversation conversation = new Conversation();
        conversation.setInitiatorId(initiatorId);
        conversation.setParticipantIds(Arrays.asList(initiatorId, "user2", "user3"));
        conversation.setStartedAt(LocalDateTime.now());
        conversation.setLastUpdated(LocalDateTime.now());
        conversation.setClosed(false);
        conversation.setArchived(false);

        when(conversationRepository.save(any(Conversation.class))).thenReturn(conversation);

        // Act
        Conversation result = conversationService.startConversation(initiatorId, participantIds);

        // Assert
        assertNotNull(result);
        assertEquals(initiatorId, result.getInitiatorId());
        assertTrue(result.getParticipantIds().containsAll(Arrays.asList(initiatorId, "user2", "user3")));
        verify(conversationRepository, times(1)).save(any(Conversation.class));
    }


    @Test
    void testGetConversationsForUser() {
        // Arrange
        String userId = "user1";
        List<Conversation> conversations = Arrays.asList(new Conversation());
        when(conversationRepository.findByParticipantIdsContaining(userId)).thenReturn(conversations);

        // Act
        List<Conversation> result = conversationService.getConversationsForUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(conversationRepository, times(1)).findByParticipantIdsContaining(userId);
    }

    @Test
    void testGetActiveConversationsForUser() {
        // Arrange
        String userId = "user1";
        List<Conversation> conversations = Arrays.asList(new Conversation());
        when(conversationRepository.findByParticipantIdsContainingAndArchivedFalse(userId)).thenReturn(conversations);

        // Act
        List<Conversation> result = conversationService.getActiveConversationsForUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(conversationRepository, times(1)).findByParticipantIdsContainingAndArchivedFalse(userId);
    }

    @Test
    void testGetArchivedConversationsForUser() {
        // Arrange
        String userId = "user1";
        List<Conversation> conversations = Arrays.asList(new Conversation());
        when(conversationRepository.findByParticipantIdsContainingAndArchivedTrue(userId)).thenReturn(conversations);

        // Act
        List<Conversation> result = conversationService.getArchivedConversationsForUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(conversationRepository, times(1)).findByParticipantIdsContainingAndArchivedTrue(userId);
    }

    @Test
    void testArchiveConversationSuccess() {
        // Arrange
        String conversationId = "conv1";
        String userId = "user1";
        Conversation conversation = new Conversation();
        conversation.setParticipantIds(Arrays.asList(userId));

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(conversationRepository.save(any(Conversation.class))).thenReturn(conversation);

        // Act
        Conversation result = conversationService.archiveConversation(conversationId, userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isArchived());
        verify(conversationRepository, times(1)).findById(conversationId);
        verify(conversationRepository, times(1)).save(conversation);
    }

    @Test
    void testArchiveConversationFailure() {
        // Arrange
        String conversationId = "conv1";
        String userId = "user1";
        Conversation conversation = new Conversation();
        conversation.setParticipantIds(Arrays.asList("user2"));

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            conversationService.archiveConversation(conversationId, userId);
        });

        assertEquals("Utilisateur non participant à la conversation", exception.getMessage());
        verify(conversationRepository, times(1)).findById(conversationId);
        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    void testCloseConversationSuccess() {
        // Arrange
        String conversationId = "conv1";
        String userId = "user1";
        Conversation conversation = new Conversation();
        conversation.setParticipantIds(Arrays.asList(userId));
        conversation.setClosed(false);

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(conversationRepository.save(any(Conversation.class))).thenReturn(conversation);

        // Act
        Conversation result = conversationService.closeConversation(conversationId, userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isClosed());
        verify(conversationRepository, times(1)).findById(conversationId);
        verify(conversationRepository, times(1)).save(conversation);
    }

    @Test
    void testCloseConversationFailure() {
        // Arrange
        String conversationId = "conv1";
        String userId = "user1";
        Conversation conversation = new Conversation();
        conversation.setParticipantIds(Arrays.asList("user2"));
        conversation.setClosed(false);

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            conversationService.closeConversation(conversationId, userId);
        });

        assertEquals("Utilisateur non participant à la conversation", exception.getMessage());
        verify(conversationRepository, times(1)).findById(conversationId);
        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    void testRestoreArchivedConversationSuccess() {
        // Arrange
        String conversationId = "conv1";
        String userId = "user1";
        Conversation conversation = new Conversation();
        conversation.setParticipantIds(Arrays.asList(userId));
        conversation.setArchived(true);

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(conversationRepository.save(any(Conversation.class))).thenReturn(conversation);

        // Act
        Conversation result = conversationService.restoreArchivedConversation(conversationId, userId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isArchived());
        verify(conversationRepository, times(1)).findById(conversationId);
        verify(conversationRepository, times(1)).save(conversation);
    }
}
