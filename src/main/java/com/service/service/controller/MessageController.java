package com.service.service.controller;

import com.service.service.model.Message;
import com.service.service.service.CommService;
import com.service.service.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.service.service.entities.MessageRequest;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private CommService commService;

    // Get all messages in a conversation
    @GetMapping("/conversation/{conversationId}/messages")
    public ResponseEntity<List<Message>> getMessagesByConversation(
            @PathVariable String conversationId,
            @RequestParam String userId) {

        List<Message> messages = messageService.getMessagesByConversation(conversationId, userId);
        return ResponseEntity.ok(messages);
    }

    // Send a message
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequest request) {
        commService.sendMessageToUser(
                request.getRecipientId(),
                request.getContent(),
                request.getTargetUserType().toString().toLowerCase()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // Mark a message as read
    @PutMapping("/read/{messageId}")
    public ResponseEntity<Message> markMessageAsRead(@PathVariable String messageId) {
        Message updatedMessage = messageService.markMessageAsRead(messageId);
        return new ResponseEntity<>(updatedMessage, HttpStatus.OK);
    }

    @PutMapping("/received/{messageId}")
    public ResponseEntity<Message> markMessageAsReceived(@PathVariable String messageId) {
        Message message = messageService.markMessageAsReceived(messageId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/restore/{messageId}")
    public ResponseEntity<Message> restoreDeletedMessage(@PathVariable String messageId, @RequestParam String userId) {
        Message restoredMessage = messageService.restoreDeletedMessage(messageId, userId);
        return new ResponseEntity<>(restoredMessage, HttpStatus.OK);
    }

    @PutMapping("/restore-archived/{messageId}")
    public ResponseEntity<Message> restoreArchivedMessage(@PathVariable String messageId, @RequestParam String userId) {
        Message restoredMessage = messageService.restoreArchivedMessage(messageId, userId);
        return new ResponseEntity<>(restoredMessage, HttpStatus.OK);
    }

    // Delete a message
    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String messageId, @RequestParam String userId) {
        messageService.deleteMessage(messageId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
