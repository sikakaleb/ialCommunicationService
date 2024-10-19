package com.service.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    private String senderId; // ID of the user sending the message (doctor, nurse, patient, etc.)
    private String recipientId; // ID of the message recipient
    private String content; // The actual message content
    private MessageType type; // Type of message (email, SMS, push notification)

    private MessageStatus status; // Status of the message (SENT, DELIVERED, READ, DELETED)
    private boolean isDeletedBySender; // Whether the message was deleted by the sender
    private boolean isDeletedByRecipient; // Whether the message was deleted by the recipient

    private LocalDateTime sentAt; // Timestamp for when the message was sent
    private LocalDateTime deliveredAt; // Timestamp for when the message was delivered
    private LocalDateTime readAt; // Timestamp for when the message was read
    private LocalDateTime receivedAt;  // Nouveau champ pour l'accusé de réception


    private String conversationId; // ID of the conversation or thread (used to link replies)
    private List<String> attachments; // List of attachment URLs or paths

    // Additional metadata
    private boolean isUrgent; // Mark if it's an urgent message (for critical cases)
    private Map<String, Object> customMetadata; // Allow any additional metadata to be stored

    private boolean archived = false;// Whether the message is archived

    // Constructor, Getters, Setters


    //region Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public boolean isDeletedBySender() {
        return isDeletedBySender;
    }

    public void setDeletedBySender(boolean deletedBySender) {
        isDeletedBySender = deletedBySender;
    }

    public boolean isDeletedByRecipient() {
        return isDeletedByRecipient;
    }

    public void setDeletedByRecipient(boolean deletedByRecipient) {
        isDeletedByRecipient = deletedByRecipient;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }

    public Map<String, Object> getCustomMetadata() {
        return customMetadata;
    }

    public void setCustomMetadata(Map<String, Object> customMetadata) {
        this.customMetadata = customMetadata;
    }

    public void setReceivedAt(LocalDateTime now) {
        this.status = MessageStatus.RECEIVED;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
    //endregion
}

