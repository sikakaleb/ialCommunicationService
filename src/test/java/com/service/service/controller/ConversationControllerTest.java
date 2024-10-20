package com.service.service.controller;


import com.service.service.entities.ConversationRequest;
import com.service.service.model.Conversation;
import com.service.service.service.ConversationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ConversationControllerTest {

    @Mock
    private ConversationService conversationService;

    @InjectMocks
    private ConversationController conversationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();
    }

    @Test
    void testGetConversationsForUser() throws Exception {
        String userId = "user1";
        List<Conversation> conversations = Arrays.asList(new Conversation(), new Conversation());

        when(conversationService.getConversationsForUser(userId)).thenReturn(conversations);

        mockMvc.perform(get("/api/conversations/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(conversations.size()));

        verify(conversationService, times(1)).getConversationsForUser(userId);
    }

    @Test
    void testGetActiveConversations() throws Exception {
        String userId = "user1";
        List<Conversation> conversations = Arrays.asList(new Conversation(), new Conversation());

        when(conversationService.getActiveConversationsForUser(userId)).thenReturn(conversations);

        mockMvc.perform(get("/api/conversations/active/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(conversations.size()));

        verify(conversationService, times(1)).getActiveConversationsForUser(userId);
    }

    @Test
    void testGetArchivedConversations() throws Exception {
        String userId = "user1";
        List<Conversation> conversations = Arrays.asList(new Conversation(), new Conversation());

        when(conversationService.getArchivedConversationsForUser(userId)).thenReturn(conversations);

        mockMvc.perform(get("/api/conversations/archived/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(conversations.size()));

        verify(conversationService, times(1)).getArchivedConversationsForUser(userId);
    }

    @Test
    void testStartConversation() throws Exception {
        ConversationRequest request = new ConversationRequest();
        request.setInitiatorId("user1");
        request.setParticipantIds(Arrays.asList("user2", "user3"));

        Conversation conversation = new Conversation();
        when(conversationService.startConversation(anyString(), anyList())).thenReturn(conversation);

        mockMvc.perform(post("/api/conversations/start")
                        .contentType("application/json")
                        .content("{\"initiatorId\": \"user1\", \"participantIds\": [\"user2\", \"user3\"]}"))
                .andExpect(status().isCreated());

        verify(conversationService, times(1)).startConversation(anyString(), anyList());
    }

    @Test
    void testCloseConversation() throws Exception {
        String conversationId = "conv1";
        String userId = "user1";
        Conversation conversation = new Conversation();

        when(conversationService.closeConversation(conversationId, userId)).thenReturn(conversation);

        mockMvc.perform(put("/api/conversations/close/{conversationId}", conversationId)
                        .param("userId", userId))
                .andExpect(status().isOk());

        verify(conversationService, times(1)).closeConversation(conversationId, userId);
    }

    @Test
    void testArchiveConversation() throws Exception {
        String conversationId = "conv1";
        String userId = "user1";
        Conversation conversation = new Conversation();

        when(conversationService.archiveConversation(conversationId, userId)).thenReturn(conversation);

        mockMvc.perform(put("/api/conversations/archive/{conversationId}", conversationId)
                        .param("userId", userId))
                .andExpect(status().isOk());

        verify(conversationService, times(1)).archiveConversation(conversationId, userId);
    }

    @Test
    void testUnarchiveConversation() throws Exception {
        String conversationId = "conv1";
        String userId = "user1";
        Conversation conversation = new Conversation();

        when(conversationService.unarchiveConversation(conversationId, userId)).thenReturn(conversation);

        mockMvc.perform(put("/api/conversations/unarchive/{conversationId}", conversationId)
                        .param("userId", userId))
                .andExpect(status().isOk());

        verify(conversationService, times(1)).unarchiveConversation(conversationId, userId);
    }

    @Test
    void testRestoreArchivedConversation() throws Exception {
        String conversationId = "conv1";
        String userId = "user1";
        Conversation conversation = new Conversation();

        when(conversationService.restoreArchivedConversation(conversationId, userId)).thenReturn(conversation);

        mockMvc.perform(put("/api/conversations/restore-archived/{conversationId}", conversationId)
                        .param("userId", userId))
                .andExpect(status().isOk());

        verify(conversationService, times(1)).restoreArchivedConversation(conversationId, userId);
    }
}
