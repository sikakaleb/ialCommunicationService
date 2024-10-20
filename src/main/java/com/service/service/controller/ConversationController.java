package com.service.service.controller;

import com.service.service.entities.ConversationRequest;
import com.service.service.model.Conversation;
import com.service.service.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;


    // Get conversations for a user
    @GetMapping("/user/{userId}")
    public List<Conversation> getConversationsForUser(@PathVariable String userId) {
        return conversationService.getConversationsForUser(userId);
    }

    // Obtenir les conversations actives d'un utilisateur
    @GetMapping("/active/{userId}")
    public ResponseEntity<List<Conversation>> getActiveConversations(@PathVariable String userId) {
        List<Conversation> conversations = conversationService.getActiveConversationsForUser(userId);
        return ResponseEntity.ok(conversations);
    }

    // Obtenir les conversations archivées d'un utilisateur
    @GetMapping("/archived/{userId}")
    public ResponseEntity<List<Conversation>> getArchivedConversations(@PathVariable String userId) {
        List<Conversation> conversations = conversationService.getArchivedConversationsForUser(userId);
        return ResponseEntity.ok(conversations);
    }

    // Start a new conversation
    @PostMapping("/start")
    public ResponseEntity<Conversation> startConversation(@RequestBody ConversationRequest request) {
        Conversation conversation = conversationService.startConversation(
                request.getInitiatorId(),
                request.getParticipantIds()
        );
        return new ResponseEntity<>(conversation, HttpStatus.CREATED);
    }


    // Close a conversation
    @PutMapping("/close/{conversationId}")
    public ResponseEntity<Conversation> closeConversation(
            @PathVariable String conversationId,
            @RequestParam String userId) {

        // Appel du service pour fermer la conversation
        Conversation closedConversation = conversationService.closeConversation(conversationId, userId);

        // Retourner la conversation fermée avec le statut HTTP 200
        return new ResponseEntity<>(closedConversation, HttpStatus.OK);
    }


    // Archiver une conversation
    @PutMapping("/archive/{conversationId}")
    public ResponseEntity<Conversation> archiveConversation(
            @PathVariable String conversationId,
            @RequestParam String userId) {
        Conversation archivedConversation = conversationService.archiveConversation(conversationId, userId);
        return ResponseEntity.ok(archivedConversation);
    }

    // Désarchiver une conversation
    @PutMapping("/unarchive/{conversationId}")
    public ResponseEntity<Conversation> unarchiveConversation(
            @PathVariable String conversationId,
            @RequestParam String userId) {
        Conversation unarchivedConversation = conversationService.unarchiveConversation(conversationId, userId);
        return ResponseEntity.ok(unarchivedConversation);
    }

    @PutMapping("/restore-archived/{conversationId}")
    public ResponseEntity<Conversation> restoreArchivedConversation(@PathVariable String conversationId, @RequestParam String userId) {
        Conversation restoredConversation = conversationService.restoreArchivedConversation(conversationId, userId);
        return new ResponseEntity<>(restoredConversation, HttpStatus.OK);
    }


}

