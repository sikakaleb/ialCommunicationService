package com.service.service.repository;

import com.service.service.model.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {

    // Find conversations for a given user (as initiator or participant)
    List<Conversation> findByParticipantIdsContaining(String participantId);

    // Find conversation by its ID
    Optional<Conversation> findById(String conversationId);

    // Trouver les conversations actives d'un utilisateur
    List<Conversation> findByParticipantIdsContainingAndArchivedTrue(String participantId);

    // Trouver les conversations archivées d'un utilisateur
    List<Conversation> findByParticipantIdsContainingAndArchivedFalse(String participantId);

    // Trouver les conversations inactives pour archivage automatique
    List<Conversation> findByLastUpdatedBeforeAndArchivedFalse(LocalDateTime cutoffDate);

    //List<Conversation> findArchivedConversationsForUser(String userId);
}

