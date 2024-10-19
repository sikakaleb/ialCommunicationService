package com.service.service.repository;

import com.service.service.model.Message;
import com.service.service.model.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    // Find all messages by conversation ID
    List<Message> findByConversationId(String conversationId);

    // Find all messages sent by a user
    List<Message> findBySenderId(String senderId);

    // Find all messages received by a user
    List<Message> findByRecipientId(String recipientId);

    // Find unread messages by recipient
    //List<Message> findByRecipientIdAndArchivedTrue(String recipientId);


    List<Message> findByConversationIdAndDeletedByRecipientFalseOrDeletedBySenderFalse(String conversationId, String userId);

    //List<Message> findDeletedMessagesForUser(String userId);

    List<Message> findBySenderIdAndIsDeletedBySenderTrue(String senderId);

    List<Message> findByRecipientIdAndIsDeletedByRecipientTrue(String recipientId);


    List<Message> findByRecipientIdAndArchivedTrue(String userId);
}
