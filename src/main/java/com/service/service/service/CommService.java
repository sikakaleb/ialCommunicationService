package com.service.service.service;

import com.service.service.entities.MessageRequest;
import com.service.service.entities.TargetUserType;
import com.service.service.model.Conversation;
import com.service.service.model.User;
import com.service.service.repository.ConversationRepository;
import com.service.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommService {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private RelativeService relativeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public void sendMessageToUsers(MessageRequest request) {
        Conversation conversation;

        // Vérifier si une conversationId est fournie
        if (request.getConversationId() != null && !request.getConversationId().isEmpty()) {
            // Récupérer la conversation existante
            conversation = conversationService.findConversationById(request.getConversationId());

            if (conversation == null) {
                throw new IllegalArgumentException("Conversation non trouvée avec cet ID");
            }

            // Si la conversation existe, ignorer les recipientIds dans la requête
        } else {
            // Si pas de conversationId, vérifier les recipientIds pour créer une nouvelle conversation
            if (request.getRecipientIds() == null || request.getRecipientIds().isEmpty()) {
                throw new IllegalArgumentException("Aucun destinataire fourni");
            }

            // Créer une nouvelle conversation avec les participants
            conversation = conversationService.findOrCreateConversation(request.getSenderId(), request.getRecipientIds());
        }

        // Sauvegarder le message pour chaque utilisateur dans la conversation
        messageService.sendMessageToGroup(
                request.getSenderId(),
                conversation.getParticipantIds(), // Utiliser les participants de la conversation
                request.getContent(),
                conversation.getId(),
                request.getType(),
                request.isUrgent()
        );

        // Envoyer le message via Kafka
        for (String recipientId : conversation.getParticipantIds()) {
            TargetUserType targetType = determineTargetUserType(recipientId); // Déterminer le type d'utilisateur
            sendToUser(recipientId, request.getContent(), targetType);
        }
    }



    private void sendToUser(String recipientId, String content, TargetUserType userType) {
        switch (userType) {
            case DOCTOR:
                doctorService.sendMessage(recipientId, content);
                break;
            case NURSE:
                nurseService.sendMessage(recipientId, content);
                break;
            case PATIENT:
                patientService.sendMessage(recipientId, content);
                break;
            case RELATIVE:
                relativeService.sendMessage(recipientId, content);
                break;
            default:
                throw new IllegalArgumentException("Invalid user type");
        }
    }

    private TargetUserType determineTargetUserType(String userId) {
        // Ici, vous pouvez interroger une base de données pour obtenir le rôle de l'utilisateur (ex : docteur, infirmière, etc.)
        // Par exemple :
        Optional<User> user = userRepository.findByExternalId(userId);
        return user.get().getTargetUserType(); // Retourne le type de l'utilisateur (DOCTOR, NURSE, etc.)
    }
}
