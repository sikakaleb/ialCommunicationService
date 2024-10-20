package com.service.service.component;

import com.service.service.model.Conversation;
import com.service.service.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ConversationArchiver {

    @Autowired
    private ConversationRepository conversationRepository;

    // Exécute tous les jours à minuit
    @Scheduled(cron = "0 0 0 * * ?")
    public void archiveInactiveConversations() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30); // Conversations inactives depuis 30 jours
        List<Conversation> inactiveConversations = conversationRepository
                .findByLastUpdatedBeforeAndArchivedFalse(cutoffDate);

        for (Conversation conversation : inactiveConversations) {
            conversation.setArchived(true);
            conversationRepository.save(conversation);
        }
    }
}