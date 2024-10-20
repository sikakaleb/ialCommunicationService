package com.service.service.service;

import com.service.service.model.Conversation;
import com.service.service.model.Message;
import com.service.service.model.MessageStatus;
import com.service.service.model.MessageType;
import com.service.service.repository.ConversationRepository;
import com.service.service.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMessage() {
        // Arrange
        String senderId = "user1";
        String recipientId = "user2";
        String content = "Hello!";
        String conversationId = "conv1";
        MessageType type = MessageType.EMAIL;
        boolean isUrgent = false;

        Message message = new Message();
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setContent(content);
        message.setConversationId(conversationId);
        message.setType(type);
        message.setStatus(MessageStatus.SENT);
        message.setSentAt(LocalDateTime.now());
        message.setUrgent(isUrgent);

        when(messageRepository.save(any(Message.class))).thenReturn(message);

        // Act
        Message result = messageService.sendMessage(senderId, recipientId, content, conversationId, type, isUrgent);

        // Assert
        assertNotNull(result);
        assertEquals(senderId, result.getSenderId());
        assertEquals(recipientId, result.getRecipientId());
        assertEquals(content, result.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void testSendMessageToGroup() {
        // Arrange
        String senderId = "user1";
        List<String> recipientIds = Arrays.asList("user2", "user3");
        String content = "Hello group!";
        String conversationId = "conv1";
        MessageType type = MessageType.SMS;
        boolean isUrgent = true;

        // Act
        messageService.sendMessageToGroup(senderId, recipientIds, content, conversationId, type, isUrgent);

        // Assert
        verify(messageRepository, times(2)).save(any(Message.class)); // 2 messages, one for each recipient
    }

    @Test
    void testGetMessagesByConversation() {
        // Arrange
        String conversationId = "conv1";
        String userId = "user1";

        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setParticipantIds(Arrays.asList("user1", "user2"));

        Message message1 = new Message();
        message1.setConversationId(conversationId);
        message1.setContent("Message 1");

        Message message2 = new Message();
        message2.setConversationId(conversationId);
        message2.setContent("Message 2");

        when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        when(messageRepository.findByConversationId(conversationId)).thenReturn(Arrays.asList(message1, message2));

        // Act
        List<Message> result = messageService.getMessagesByConversation(conversationId, userId);

        // Assert
        assertEquals(2, result.size());
        verify(conversationRepository, times(1)).findById(conversationId);
        verify(messageRepository, times(1)).findByConversationId(conversationId);
    }

    @Test
    void testMarkMessageAsRead() {
        // Arrange
        String messageId = "msg1";
        Message message = new Message();
        message.setId(messageId);
        message.setStatus(MessageStatus.SENT);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(message); // Simuler le retour de la sauvegarde

        // Act
        Message result = messageService.markMessageAsRead(messageId);

        // Assert
        assertNotNull(result); // Vérifiez que le résultat n'est pas null
        assertEquals(MessageStatus.READ, result.getStatus());
        assertNotNull(result.getReadAt());
        verify(messageRepository, times(1)).save(message);
    }


    @Test
    void testDeleteMessage() {
        // Arrange
        String messageId = "msg1";
        String userId = "user1"; // The user who is trying to delete the message
        Message message = new Message();
        message.setId(messageId);
        message.setSenderId(userId); // Set the user as the sender of the message
        message.setRecipientId("user2"); // Also set a recipient ID

        // Simuler le comportement du repository pour retourner le message
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // Act
        messageService.deleteMessage(messageId, userId); // Simule la suppression du message

        // Assert
        assertTrue(message.isDeletedBySender(), "Le message doit être marqué comme supprimé par l'expéditeur");
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void testDeleteMessageAsRecipient() {
        // Arrange
        String messageId = "msg1";
        String userId = "user2"; // The recipient who is trying to delete the message
        Message message = new Message();
        message.setId(messageId);
        message.setSenderId("user1"); // The sender is a different user
        message.setRecipientId(userId); // The user is the recipient

        // Simuler le comportement du repository pour retourner le message
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // Act
        messageService.deleteMessage(messageId, userId); // Simule la suppression du message par le destinataire

        // Assert
        assertTrue(message.isDeletedByRecipient(), "Le message doit être marqué comme supprimé par le destinataire");
        verify(messageRepository, times(1)).save(message);
    }


    @Test
    void testRestoreDeletedMessage() {
        // Arrange
        String messageId = "msg1";
        String userId = "user1";
        Message message = new Message();
        message.setId(messageId);
        message.setSenderId(userId);
        message.setRecipientId("user2");
        message.setDeletedBySender(true);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(message); // Simuler le retour de la sauvegarde

        // Act
        Message result = messageService.restoreDeletedMessage(messageId, userId);

        // Assert
        assertNotNull(result); // Vérifiez que le résultat n'est pas null
        assertFalse(result.isDeletedBySender()); // Vérifiez que le message n'est plus marqué comme supprimé par l'expéditeur
        verify(messageRepository, times(1)).save(message);
    }


    @Test
    void testRestoreDeletedMessageByRecipient() {
        // Arrange
        String messageId = "msg1";
        String userId = "user2"; // Simuler que 'user2' est le destinataire
        Message message = new Message();
        message.setId(messageId);
        message.setSenderId("user1"); // Définir l'expéditeur comme étant un autre utilisateur
        message.setRecipientId(userId); // Simuler que 'user2' est le destinataire
        message.setDeletedByRecipient(true); // Simuler que le message a été supprimé par le destinataire

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(message); // Simuler le retour de la sauvegarde

        // Act
        Message result = messageService.restoreDeletedMessage(messageId, userId); // Restaurer pour le destinataire 'user2'

        // Assert
        assertNotNull(result); // Vérifiez que le résultat n'est pas null
        assertFalse(result.isDeletedByRecipient()); // Vérifier que le message n'est plus marqué comme supprimé par le destinataire
        verify(messageRepository, times(1)).save(message);
    }



    @Test
    void testGetArchivedMessagesForUser() {
        // Arrange
        String userId = "user1";
        Message message1 = new Message();
        message1.setRecipientId(userId);
        message1.setArchived(true);

        when(messageRepository.findByRecipientIdAndArchivedTrue(userId)).thenReturn(Collections.singletonList(message1));

        // Act
        List<Message> result = messageService.getArchivedMessagesForUser(userId);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).isArchived());
        verify(messageRepository, times(1)).findByRecipientIdAndArchivedTrue(userId);
    }
}
