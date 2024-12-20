package com.argel6767.tailor.ai.chat_session;

import com.argel6767.tailor.ai.user.User;
import com.argel6767.tailor.ai.user.UserRepository;
import com.argel6767.tailor.ai.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatSessionServiceTest {

    @Mock
    private ChatSessionRepository chatSessionRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private ChatSessionService chatSessionService;
    private final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        chatSessionService = new ChatSessionService(chatSessionRepository, userService, userRepository);
    }

    @Test
    void testAddChatSession_Success() {
        // Arrange
        ChatSession chatSession = new ChatSession();
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(chatSession);

        // Act
        ChatSession result = chatSessionService.addChatSession(chatSession);

        // Assert
        assertNotNull(result);
        verify(chatSessionRepository).save(chatSession);
    }

    @Test
    void testCreateChatSession_Success() {
        // Arrange
        User user = new User();
        user.setChatSessions(new ArrayList<>());
        ChatSession chatSession = new ChatSession();

        when(userService.getUserByEmail(TEST_EMAIL)).thenReturn(user);
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(chatSession);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ChatSession result = chatSessionService.createChatSession(TEST_EMAIL);

        // Assert
        assertNotNull(result);
        verify(chatSessionRepository, times(2)).save(any(ChatSession.class));
        verify(userRepository).save(user);
    }

    @Test
    void testGetAllUserChatSessions_Success() {
        // Arrange
        User user = new User();
        List<ChatSession> chatSessions = new ArrayList<>();
        chatSessions.add(new ChatSession());
        user.setChatSessions(chatSessions);

        when(userService.getUserByEmail(TEST_EMAIL)).thenReturn(user);

        // Act
        ResponseEntity<List<ChatSession>> response = chatSessionService.getAllUserChatSessions(TEST_EMAIL);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(chatSessions, response.getBody());
    }

    @Test
    void testGetChatSession_Success() {
        // Arrange
        Long chatSessionId = 1L;
        ChatSession chatSession = new ChatSession();
        when(chatSessionRepository.findById(chatSessionId)).thenReturn(Optional.of(chatSession));

        // Act
        ChatSession result = chatSessionService.getChatSession(chatSessionId);

        // Assert
        assertNotNull(result);
        assertEquals(chatSession, result);
    }

    @Test
    void testGetChatSession_NotFound() {
        // Arrange
        Long chatSessionId = 1L;
        when(chatSessionRepository.findById(chatSessionId)).thenReturn(Optional.empty());

        // Act
        ChatSession result = chatSessionService.getChatSession(chatSessionId);

        // Assert
        assertNull(result);
    }

    @Test
    void testDeleteChatSession_Success() {
        // Arrange
        Long chatSessionId = 1L;
        ChatSession chatSession = new ChatSession();
        when(chatSessionRepository.findById(chatSessionId)).thenReturn(Optional.of(chatSession));

        // Act
        ResponseEntity<ChatSession> response = chatSessionService.deleteChatSession(chatSessionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chatSession, response.getBody());
        verify(chatSessionRepository).deleteById(chatSessionId);
    }

    @Test
    void testDeleteChatSession_NotFound() {
        // Arrange
        Long chatSessionId = 1L;
        when(chatSessionRepository.findById(chatSessionId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ChatSession> response = chatSessionService.deleteChatSession(chatSessionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(chatSessionRepository, never()).deleteById(any());
    }

    @Test
    void testSaveChatSession_Success() {
        // Arrange
        ChatSession chatSession = new ChatSession();
        when(chatSessionRepository.save(chatSession)).thenReturn(chatSession);

        // Act
        ChatSession result = chatSessionService.saveChatSession(chatSession);

        // Assert
        assertNotNull(result);
        verify(chatSessionRepository).save(chatSession);
    }

    @Test
    void testUpdateChatSessionName_Success() {
        // Arrange
        Long chatSessionId = 1L;
        String newName = "New Session Name";
        ChatSession chatSession = new ChatSession();

        when(chatSessionRepository.findById(chatSessionId)).thenReturn(Optional.of(chatSession));
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(chatSession);

        // Act
        ResponseEntity<?> response = chatSessionService.updateChatSessionName(chatSessionId, newName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newName, chatSession.getChatSessionName());
        verify(chatSessionRepository).save(chatSession);
    }

    @Test
    void testUpdateChatSessionName_NotFound() {
        // Arrange
        Long chatSessionId = 1L;
        String newName = "New Session Name";
        when(chatSessionRepository.findById(chatSessionId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = chatSessionService.updateChatSessionName(chatSessionId, newName);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(chatSessionRepository, never()).save(any());
    }
}