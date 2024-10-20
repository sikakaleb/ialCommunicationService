package com.service.service.model;

public enum MessageStatus {
    SENT, // Message has been sent
    DELIVERED, // Message has been delivered to the recipient
    READ, // Message has been read by the recipient
    RECEIVED, DELETED // Message has been deleted
}

