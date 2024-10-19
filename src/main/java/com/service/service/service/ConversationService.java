package com.service.service.service;

import com.service.service.model.Conversation;
import com.service.service.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    // Créer une nouvelle conversation entre plusieurs participants
    public Conversation startConversation(String initiatorId, List<String> participantIds) {
        Conversation conversation = new Conversation();
        conversation.setInitiatorId(initiatorId);
        participantIds.add(initiatorId); // Inclure l'initiateur dans les participants
        conversation.setParticipantIds(participantIds);
        conversation.setStartedAt(LocalDateTime.now());
        conversation.setLastUpdated(LocalDateTime.now());
        conversation.setClosed(false);
        conversation.setArchived(false); // Initialiser comme non archivée
        return conversationRepository.save(conversation);
    }

    // Get all conversations for a user
    public List<Conversation> getConversationsForUser(String userId) {
        return conversationRepository.findByParticipantIdsContaining(userId);
    }

    // Obtenir toutes les conversations actives d'un utilisateur (non archivées)
    public List<Conversation> getActiveConversationsForUser(String userId) {
        return conversationRepository.findByParticipantIdsContainingAndArchivedFalse(userId);
    }

    // Obtenir toutes les conversations archivées d'un utilisateur
    public List<Conversation> getArchivedConversationsForUser(String userId) {
        return conversationRepository.findByParticipantIdsContainingAndArchivedTrue(userId);
    }

    // Archiver une conversation (suppression logique)
    public Conversation archiveConversation(String conversationId, String userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée"));

        if (conversation.getParticipantIds().contains(userId)) {
            conversation.setArchived(true);
            conversation.setLastUpdated(LocalDateTime.now());
            return conversationRepository.save(conversation);
        } else {
            throw new RuntimeException("Utilisateur non participant à la conversation");
        }
    }

    // Désarchiver une conversation
    public Conversation unarchiveConversation(String conversationId, String userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée"));

        if (conversation.getParticipantIds().contains(userId)) {
            conversation.setArchived(false);
            conversation.setLastUpdated(LocalDateTime.now());
            return conversationRepository.save(conversation);
        } else {
            throw new RuntimeException("Utilisateur non participant à la conversation");
        }
    }

    // Fermer une conversation
    // Fermer une conversation
    public Conversation closeConversation(String conversationId, String userId) {
        // Récupération de la conversation
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée"));

        // Vérification que l'utilisateur est un participant
        if (!conversation.getParticipantIds().contains(userId)) {
            throw new RuntimeException("Utilisateur non participant à la conversation");
        }

        // Vérification si la conversation est déjà fermée
        if (conversation.isClosed()) {
            throw new RuntimeException("La conversation est déjà fermée");
        }

        // Mise à jour du statut de la conversation
        conversation.setClosed(true);
        conversation.setLastUpdated(LocalDateTime.now());

        // Sauvegarde de l'état de la conversation
        return conversationRepository.save(conversation);
    }

    // Restaurer une conversation archivée
    public Conversation restoreArchivedConversation(String conversationId, String userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée"));

        if (conversation.isArchived() && conversation.getParticipantIds().contains(userId)) {
            conversation.setArchived(false);
            conversation.setLastUpdated(LocalDateTime.now());
            return conversationRepository.save(conversation);
        } else {
            throw new RuntimeException("Accès refusé ou conversation non archivée");
        }
    }

    // Obtenir toutes les conversations archivées pour un utilisateur
    /*public List<Conversation> getArchivedConversationsForUser(String userId) {
        return conversationRepository.findArchivedConversationsForUser(userId);
    }*/


}

