package com.service.service.controller;


import com.service.service.entities.MessageRequest;
import com.service.service.model.Message;
import com.service.service.model.MessageStatus;
import com.service.service.model.MessageType;
import com.service.service.service.CommService;
import com.service.service.service.MessageService;
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

class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private CommService commService;

    @InjectMocks
    private MessageController messageController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    void testGetMessagesByConversation() throws Exception {
        String conversationId = "conv1";
        String userId = "user1";
        List<Message> messages = Arrays.asList(new Message(), new Message());

        when(messageService.getMessagesByConversation(conversationId, userId)).thenReturn(messages);

        mockMvc.perform(get("/api/messages/conversation/{conversationId}/messages", conversationId)
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(messages.size()));

        verify(messageService, times(1)).getMessagesByConversation(conversationId, userId);
    }

    @Test
    void testSendMessage() throws Exception {
        MessageRequest request = new MessageRequest();
        request.setRecipientId("user2");
        request.setContent("Hello!");
        request.setType(MessageType.EMAIL); // Ex: EMAIL, SMS, etc.

        mockMvc.perform(post("/api/messages/send")
                        .contentType("application/json")
                        .content("{\"recipientId\": \"user2\", \"content\": \"Hello!\", \"type\": \"EMAIL\"}"))
                .andExpect(status().isOk());

        verify(commService, times(1)).sendMessageToUser(eq("user2"), eq("Hello!"), eq("email"));
    }

    @Test
    void testMarkMessageAsRead() throws Exception {
        String messageId = "msg1";
        Message message = new Message();
        message.setId(messageId);
        message.setStatus(MessageStatus.READ);

        when(messageService.markMessageAsRead(messageId)).thenReturn(message);

        mockMvc.perform(put("/api/messages/read/{messageId}", messageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(MessageStatus.READ.toString()));

        verify(messageService, times(1)).markMessageAsRead(messageId);
    }

    @Test
    void testMarkMessageAsReceived() throws Exception {
        String messageId = "msg1";
        Message message = new Message();
        message.setId(messageId);
        message.setStatus(MessageStatus.RECEIVED);

        when(messageService.markMessageAsReceived(messageId)).thenReturn(message);

        mockMvc.perform(put("/api/messages/received/{messageId}", messageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(MessageStatus.RECEIVED.toString()));

        verify(messageService, times(1)).markMessageAsReceived(messageId);
    }

    @Test
    void testRestoreDeletedMessage() throws Exception {
        String messageId = "msg1";
        String userId = "user1";
        Message message = new Message();
        message.setId(messageId);

        when(messageService.restoreDeletedMessage(messageId, userId)).thenReturn(message);

        mockMvc.perform(put("/api/messages/restore/{messageId}", messageId)
                        .param("userId", userId))
                .andExpect(status().isOk());

        verify(messageService, times(1)).restoreDeletedMessage(messageId, userId);
    }

    @Test
    void testRestoreArchivedMessage() throws Exception {
        String messageId = "msg1";
        String userId = "user1";
        Message message = new Message();
        message.setId(messageId);

        when(messageService.restoreArchivedMessage(messageId, userId)).thenReturn(message);

        mockMvc.perform(put("/api/messages/restore-archived/{messageId}", messageId)
                        .param("userId", userId))
                .andExpect(status().isOk());

        verify(messageService, times(1)).restoreArchivedMessage(messageId, userId);
    }

    @Test
    void testDeleteMessage() throws Exception {
        String messageId = "msg1";
        String userId = "user1";

        mockMvc.perform(delete("/api/messages/delete/{messageId}", messageId)
                        .param("userId", userId))
                .andExpect(status().isNoContent());

        verify(messageService, times(1)).deleteMessage(messageId, userId);
    }
}
