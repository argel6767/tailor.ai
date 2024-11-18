package com.argel6767.tailor.ai.message;

import com.argel6767.tailor.ai.chat_session.ChatSession;
import com.argel6767.tailor.ai.chat_session.ChatSessionService;
import com.argel6767.tailor.ai.message.requests.NewMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    private static final Long CHAT_SESSION_ID = 1L;
    private static final Author TEST_AUTHOR = Author.USER;
    private static final String TEST_MESSAGE = "Test Message";

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatSessionService chatSessionService;

    @InjectMocks
    private MessageService messageService;

    private ChatSession testChatSession;
    private Message testMessage;
    private NewMessageRequest testNewMessageRequest;

    @BeforeEach
    void setUp() {
        // Set up ChatSession
        testChatSession = new ChatSession();
        testChatSession.setChatSessionId(CHAT_SESSION_ID);
        testChatSession.setMessages(new ArrayList<>());

        // Set up Message
        testMessage = new Message();
        testMessage.setAuthor(TEST_AUTHOR);
        testMessage.setBody(TEST_MESSAGE);
        testMessage.setChatSession(testChatSession);

        // Set up NewMessageRequest
        testNewMessageRequest = new NewMessageRequest();
        testNewMessageRequest.setAuthor(TEST_AUTHOR);
        testNewMessageRequest.setMessage(TEST_MESSAGE);
    }

    @Test
    void testAddMessageSuccess() {
        // Arrange
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        // Act
        ResponseEntity<Message> result = messageService.addMessage(testMessage);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_AUTHOR, result.getBody().getAuthor());
        assertEquals(TEST_MESSAGE, result.getBody().getBody());
        verify(messageRepository).save(testMessage);
    }

    @Test
    void testCreateMessageSuccessful() {
        // Arrange
        when(chatSessionService.getChatSession(CHAT_SESSION_ID)).thenReturn(testChatSession);
        when(chatSessionService.saveChatSession(any(ChatSession.class))).thenReturn(testChatSession);

        // Act
        ResponseEntity<Message> response = messageService.createMessage(testNewMessageRequest, CHAT_SESSION_ID);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(TEST_AUTHOR, response.getBody().getAuthor());
        assertEquals(TEST_MESSAGE, response.getBody().getBody());
        assertEquals(1, testChatSession.getMessages().size());
        verify(chatSessionService).getChatSession(CHAT_SESSION_ID);
        verify(chatSessionService).saveChatSession(testChatSession);
    }

    @Test
    void testCreateMessageWithNonExistentChatSession() {
        // Arrange
        when(chatSessionService.getChatSession(CHAT_SESSION_ID)).thenReturn(null);

        // Act
        ResponseEntity<Message> response = messageService.createMessage(testNewMessageRequest, CHAT_SESSION_ID);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
        verify(chatSessionService).getChatSession(CHAT_SESSION_ID);
        verify(chatSessionService, never()).saveChatSession(any());
    }

    @Test
    void testGetAllChatSessionMessagesSuccessful() {
        // Arrange
        List<Message> messages = Arrays.asList(testMessage);
        testChatSession.setMessages(messages);
        when(chatSessionService.getChatSession(CHAT_SESSION_ID)).thenReturn(testChatSession);

        // Act
        ResponseEntity<List<Message>> response = messageService.getAllChatSessionMessages(CHAT_SESSION_ID);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(TEST_AUTHOR, response.getBody().get(0).getAuthor());
        assertEquals(TEST_MESSAGE, response.getBody().get(0).getBody());
        verify(chatSessionService).getChatSession(CHAT_SESSION_ID);
    }

    @Test
    void testGetAllChatSessionMessagesWithNonExistentChatSession() {
        // Arrange
        when(chatSessionService.getChatSession(CHAT_SESSION_ID)).thenReturn(null);

        // Act
        ResponseEntity<List<Message>> response = messageService.getAllChatSessionMessages(CHAT_SESSION_ID);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
        verify(chatSessionService).getChatSession(CHAT_SESSION_ID);
    }

    @Test
    void testGetAllChatSessionMessagesWithEmptyMessageList() {
        // Arrange
        when(chatSessionService.getChatSession(CHAT_SESSION_ID)).thenReturn(testChatSession);

        // Act
        ResponseEntity<List<Message>> response = messageService.getAllChatSessionMessages(CHAT_SESSION_ID);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(chatSessionService).getChatSession(CHAT_SESSION_ID);
    }
}