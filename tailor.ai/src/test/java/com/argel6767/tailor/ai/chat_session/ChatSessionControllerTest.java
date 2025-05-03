package com.argel6767.tailor.ai.chat_session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatSessionControllerTest {

    @Mock
    private ChatSessionService chatSessionService;

    private ChatSessionController chatSessionController;
    private final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        chatSessionController = new ChatSessionController(chatSessionService);
    }

    @Test
    void testCreateChatSession_Success() {
        // Arrange
        ChatSession chatSession = new ChatSession();
        when(chatSessionService.createChatSession(TEST_EMAIL)).thenReturn(chatSession);

        // Act
        ResponseEntity<ChatSession> response = chatSessionController.createChatSession();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chatSession, response.getBody());
        verify(chatSessionService).createChatSession(TEST_EMAIL);
    }

    @Test
    void testGetUserChatSessions_Success() {
        // Arrange
        List<ChatSession> chatSessions = new ArrayList<>();
        chatSessions.add(new ChatSession());
        ResponseEntity<List<ChatSession>> expectedResponse = ResponseEntity.ok(chatSessions);

        when(chatSessionService.getAllUserChatSessions(TEST_EMAIL)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<List<ChatSession>> response = chatSessionController.getUserChatSessions();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chatSessions, response.getBody());
        verify(chatSessionService).getAllUserChatSessions(TEST_EMAIL);
    }

    @Test
    void testGetChatSession_Success() {
        // Arrange
        Long chatSessionId = 1L;
        ChatSession chatSession = new ChatSession();
        when(chatSessionService.getChatSession(chatSessionId)).thenReturn(chatSession);

        // Act
        ResponseEntity<ChatSession> response = chatSessionController.getChatSession(chatSessionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chatSession, response.getBody());
        verify(chatSessionService).getChatSession(chatSessionId);
    }

    @Test
    void testGetChatSession_NotFound() {
        // Arrange
        Long chatSessionId = 1L;
        when(chatSessionService.getChatSession(chatSessionId)).thenReturn(null);

        // Act
        ResponseEntity<ChatSession> response = chatSessionController.getChatSession(chatSessionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(chatSessionService).getChatSession(chatSessionId);
    }

    @Test
    void testDeleteChatSession_Success() {
        // Arrange
        Long chatSessionId = 1L;
        ChatSession chatSession = new ChatSession();
        ResponseEntity<ChatSession> expectedResponse = ResponseEntity.ok(chatSession);

        when(chatSessionService.deleteChatSession(chatSessionId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ChatSession> response = chatSessionController.deleteChatSession(chatSessionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chatSession, response.getBody());
        verify(chatSessionService).deleteChatSession(chatSessionId);
    }

    @Test
    void testDeleteChatSession_NotFound() {
        // Arrange
        Long chatSessionId = 1L;
        ResponseEntity<ChatSession> expectedResponse = ResponseEntity.notFound().build();

        when(chatSessionService.deleteChatSession(chatSessionId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ChatSession> response = chatSessionController.deleteChatSession(chatSessionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(chatSessionService).deleteChatSession(chatSessionId);
    }

    @Test
    void testUpdateChatSessionName_Success() {
        // Arrange
        Long chatSessionId = 1L;
        String newName = "New Session Name";
        ChatSession updatedSession = new ChatSession();
        updatedSession.setChatSessionName(newName);
        ResponseEntity<ChatSession> expectedResponse = ResponseEntity.ok(updatedSession);

        when(chatSessionService.updateChatSessionName(chatSessionId, newName)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> response = chatSessionController.updateChatSessionName(chatSessionId, newName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(chatSessionService).updateChatSessionName(chatSessionId, newName);
    }

    @Test
    void testUpdateChatSessionName_NotFound() {
        // Arrange
        Long chatSessionId = 1L;
        String newName = "New Session Name";
        ResponseEntity<?> expectedResponse = ResponseEntity.notFound().build();

        when(chatSessionService.updateChatSessionName(chatSessionId, newName)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // Act
        ResponseEntity<?> response = chatSessionController.updateChatSessionName(chatSessionId, newName);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(chatSessionService).updateChatSessionName(chatSessionId, newName);
    }
}