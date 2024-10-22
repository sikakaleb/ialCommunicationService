package com.service.service.service;

import com.service.service.entities.TargetUserType;
import com.service.service.model.Conversation;
import com.service.service.model.User;
import com.service.service.repository.ConversationRepository;
import com.service.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UserRepository userRepository;

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


    // Méthode pour déterminer le type d'utilisateur à partir de l'ID
    private TargetUserType determineTargetUserType(String userId) {
        if (userId.startsWith("doctor")) {
            return TargetUserType.DOCTOR;
        } else if (userId.startsWith("nurse")) {
            return TargetUserType.NURSE;
        } else if (userId.startsWith("patient")) {
            return TargetUserType.PATIENT;
        } else if (userId.startsWith("relative")) {
            return TargetUserType.RELATIVE;
        } else {
            throw new IllegalArgumentException("Type d'utilisateur non reconnu pour l'ID : " + userId);
        }
    }

    // Vérifier si un utilisateur existe, sinon le créer
    private void ensureUserExists(String userId) {
        Optional<User> existingUser = userRepository.findByExternalId(userId);
        if (!existingUser.isPresent()) {
            // Créer un nouvel utilisateur avec l'ID fourni
            User newUser = new User();
            newUser.setExternalId(userId);
            newUser.setTargetUserType(determineTargetUserType(userId));
            userRepository.save(newUser);
        }
    }

    // Vérifier si une conversation existe entre les participants, sinon la créer
    public Conversation findOrCreateConversation(String initiatorId, List<String> participantIds) {
        // Ajouter l'initiateur à la liste des participants
        List<String> allParticipants = new ArrayList<>(participantIds);
        allParticipants.add(initiatorId);

        // Rechercher les conversations contenant tous les participants
        List<Conversation> existingConversations = conversationRepository.findByParticipants(allParticipants);

        // Vérifier si une des conversations récupérées a exactement le même ensemble de participants
        for (Conversation conversation : existingConversations) {
            Set<String> conversationParticipants = new HashSet<>(conversation.getParticipantIds());
            Set<String> requestParticipants = new HashSet<>(allParticipants);

            if (conversationParticipants.equals(requestParticipants)) {
                // Les ensembles sont exactement les mêmes
                return conversation;
            }
        }

        // Si aucune conversation correspondante n'a été trouvée, en créer une nouvelle
        return startConversation(initiatorId, participantIds);
    }


    // Créer une nouvelle conversation entre plusieurs participants
    public Conversation startConversation(String initiatorId, List<String> participantIds) {
        // Assurez-vous que l'initiateur et les participants existent
        ensureUserExists(initiatorId);
        participantIds.forEach(this::ensureUserExists);

        // Créer une nouvelle liste modifiable pour les participants
        List<String> participants = new ArrayList<>(participantIds);
        participants.add(initiatorId); // Inclure l'initiateur dans les participants

        Conversation conversation = new Conversation();
        conversation.setInitiatorId(initiatorId);
        conversation.setParticipantIds(participants);
        conversation.setStartedAt(LocalDateTime.now());
        conversation.setLastUpdated(LocalDateTime.now());
        conversation.setClosed(false);
        conversation.setArchived(false); // Initialiser comme non archivée

        return conversationRepository.save(conversation);
    }

    public Conversation findConversationById(String conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation non trouvée"));
    }



}

