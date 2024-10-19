package com.service.service.entities;

import java.util.List;

public class ConversationRequest {
    private String initiatorId;
    private List<String> participantIds;

    // Getters and Setters


    public String getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }
}

