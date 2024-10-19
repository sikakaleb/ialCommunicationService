package com.service.service.service;

import com.service.service.model.Conversation;
import com.service.service.model.Message;
import com.service.service.model.MessageStatus;
import com.service.service.model.MessageType;
import com.service.service.repository.ConversationRepository;
import com.service.service.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    // Send a new message
    public Message sendMessage(String senderId, String recipientId, String content, String conversationId, MessageType type, boolean isUrgent) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setContent(content);
        message.setConversationId(conversationId);
        message.setType(type);
        message.setStatus(MessageStatus.SENT);
        message.setSentAt(LocalDateTime.now());
        message.setUrgent(isUrgent);

        return messageRepository.save(message);
    }

    // Envoyer un message à plusieurs destinataires dans une conversation de groupe
    public void sendMessageToGroup(String senderId, List<String> recipientIds, String content, String conversationId, MessageType type, boolean isUrgent) {
        for (String recipientId : recipientIds) {
            Message message = new Message();
            message.setSenderId(senderId);
            message.setRecipientId(recipientId);
            message.setContent(content);
            message.setConversationId(conversationId);
            message.setType(type);
            message.setStatus(MessageStatus.SENT);
            message.setSentAt(LocalDateTime.now());
            message.setUrgent(isUrgent);
            message.setReceivedAt(null);  // initialement nul
            message.setReadAt(null);  // initialement nul

            messageRepository.save(message);
        }
    }

    // Obtenir tous les messages dans une conversation
    public List<Message> getMessagesByConversation(String conversationId, String userId) {
        // Récupération de la conversation par son ID
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée"));

        // Vérification si la conversation est archivée et que l'utilisateur est bien un participant
        if (conversation.isArchived() && !conversation.getParticipantIds().contains(userId)) {
            throw new RuntimeException("Accès refusé ou conversation archivée");
        }

        // Vérification que l'utilisateur est bien un participant de la conversation
        if (!conversation.getParticipantIds().contains(userId)) {
            throw new RuntimeException("Utilisateur non participant à la conversation");
        }

        // Récupérer les messages pour la conversation
        return messageRepository.findByConversationId(conversationId);
    }


    // Get all messages sent by a user
    public List<Message> getMessagesSentByUser(String senderId) {
        return messageRepository.findBySenderId(senderId);
    }

    // Get all unread messages for a user
    public List<Message> getUnreadMessages(String recipientId) {
        return messageRepository.findByRecipientIdAndStatus(recipientId, MessageStatus.SENT);
    }

    // Mark a message as read
    public Message markMessageAsRead(String messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
        message.setStatus(MessageStatus.READ);
        message.setReadAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    // Delete a message for the sender or recipient
    public void deleteMessage(String messageId, String userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message non trouvé"));

        // Si l'utilisateur est l'expéditeur
        if (message.getSenderId().equals(userId)) {
            message.setDeletedBySender(true);
        }

        // Si l'utilisateur est le destinataire
        if (message.getRecipientId().equals(userId)) {
            message.setDeletedByRecipient(true);
        }

        messageRepository.save(message);
    }

    public Message markMessageAsReceived(String messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message non trouvé"));

        message.setReceivedAt(LocalDateTime.now());
        message.setStatus(MessageStatus.RECEIVED);
        return messageRepository.save(message);
    }

    // Obtenir tous les messages non supprimés par l'utilisateur
    public List<Message> getMessagesForUser(String conversationId, String userId) {
        return messageRepository.findByConversationIdAndDeletedByRecipientFalseOrDeletedBySenderFalse(conversationId, userId);
    }

}

