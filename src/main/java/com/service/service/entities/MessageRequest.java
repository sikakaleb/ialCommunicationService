package com.service.service.entities;

import com.service.service.model.MessageType;

import java.util.List;

public class MessageRequest {
    private String senderId;
    private List<String> recipientIds; // Modification : liste des destinataires
    private String content;
    private String conversationId;
    private MessageType type;
    private boolean isUrgent;
    private TargetUserType targetUserType; // Utilisation de l'énumération


    // Getters and Setters


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public List<String> getRecipientIds() {
        return recipientIds;
    }

    public void setRecipientId(List<String> recipientId) {
        this.recipientIds = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }

    public TargetUserType getTargetUserType() {
        return targetUserType;
    }

    public void setTargetUserType(TargetUserType targetUserType) {
        this.targetUserType = targetUserType;
    }
}
