package com.argel6767.tailor.ai.message;

import com.argel6767.tailor.ai.message.requests.NewMessageRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @Test
    void testAddMessage() {
        // Arrange
        Message inputMessage = new Message();
        Message returnedMessage = new Message();
        ResponseEntity<Message> expectedResponse = ResponseEntity.ok(returnedMessage);

        when(messageService.addMessage(any(Message.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Message> actualResponse = messageController.addMessage(inputMessage);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(messageService, times(1)).addMessage(any(Message.class));
    }

    @Test
    void testCreateMessage() {
        // Arrange
        Long chatSessionId = 1L;
        NewMessageRequest newMessageRequest = new NewMessageRequest();
        Message returnedMessage = new Message();
        ResponseEntity<Message> expectedResponse = ResponseEntity.ok(returnedMessage);

        when(messageService.createMessage(any(NewMessageRequest.class), eq(chatSessionId))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Message> actualResponse = messageController.createMessage(chatSessionId, newMessageRequest);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(messageService, times(1)).createMessage(any(NewMessageRequest.class), eq(chatSessionId));
    }

    @Test
    void testGetAllChatMessages() {
        // Arrange
        Long chatSessionId = 1L;
        List<Message> messages = List.of(new Message(), new Message());
        ResponseEntity<List<Message>> expectedResponse = ResponseEntity.ok(messages);

        when(messageService.getAllChatSessionMessages(eq(chatSessionId))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<List<Message>> actualResponse = messageController.getAllChatMessages(chatSessionId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(messageService, times(1)).getAllChatSessionMessages(eq(chatSessionId));
    }
}

