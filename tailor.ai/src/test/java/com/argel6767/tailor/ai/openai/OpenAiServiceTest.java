package com.argel6767.tailor.ai.openai;

import com.argel6767.tailor.ai.message.Author;
import com.argel6767.tailor.ai.message.Message;
import com.argel6767.tailor.ai.message.MessageService;
import com.argel6767.tailor.ai.message.requests.NewMessageRequest;
import com.argel6767.tailor.ai.message.utils.MessageHistoryFlattener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OpenAiServiceTest {

    private OpenAiService openAiService;
    private ChatClient chatClient;
    private MessageService messageService;

    @BeforeEach
    public void setup() {
        chatClient = mock(ChatClient.class);
        messageService = mock(MessageService.class);
        openAiService = new OpenAiService(chatClient, messageService);
    }

    @Test
    public void testGetAiResponse() {
        Long chatSessionId = 1L;
        String userMessage = "Hello, how are you?";

        // Mock the message history
        Message message1 = new Message();
        message1.setBody("Hell World");
        message1.setAuthor(Author.ASSISTANT);
        Message message2 = new Message();
        message2.setBody("How are you?");
        message2.setAuthor(Author.USER);
        List<Message> messageHistory = Arrays.asList(message1, message2);
        ResponseEntity<List<Message>> responseEntity = ResponseEntity.ok(messageHistory);

        when(messageService.getAllChatSessionMessages(chatSessionId)).thenReturn(responseEntity);

        // Mock the flattening of message history
        String flattenedHistory = "Assistant: Hi there!\nUser: I am fine, thank you.";
        String completeHistory = flattenedHistory + "\nUser: " + userMessage;

        // Optionally mock MessageHistoryFlattener if it's a dependency
        // If MessageHistoryFlattener is static, you can use a utility like PowerMockito to mock it
        // For this example, we assume it works as intended

        // Mock the chain of calls on chatClient
        Prompt promptMock = mock(Prompt.class, RETURNS_SELF);
        when(chatClient.prompt()).thenReturn((ChatClient.ChatClientRequestSpec) promptMock);
        when(((ChatClient.ChatClientRequestSpec) promptMock).user(completeHistory)).thenReturn((ChatClient.ChatClientRequestSpec) promptMock);
        when(((ChatClient.ChatClientRequestSpec) promptMock).stream()).thenReturn((ChatClient.StreamResponseSpec) promptMock);

        // Mock the AI response
        Flux<String> aiResponseFlux = Flux.just("I'm doing well", ", thank you!");
        when(((ChatClient.StreamResponseSpec) promptMock).content()).thenReturn(aiResponseFlux);

        // Mock the messageService.createMessage() method
        when(messageService.createMessage(any(NewMessageRequest.class), eq(chatSessionId))).thenReturn(null);

        // Call the method under test
       Object resultFlux = openAiService.getAiResponse(chatSessionId, userMessage);

        // Collect the emitted items
        List<String> resultList = new ArrayList<>();

        // Verify the result
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("I'm doing well", resultList.get(0));
        assertEquals(", thank you!", resultList.get(1));

        // Verify that messageService.createMessage() was called with the correct parameters
        ArgumentCaptor<NewMessageRequest> argumentCaptor = ArgumentCaptor.forClass(NewMessageRequest.class);
        verify(messageService).createMessage(argumentCaptor.capture(), eq(chatSessionId));

        NewMessageRequest capturedRequest = argumentCaptor.getValue();
        assertEquals("I'm doing well, thank you!", capturedRequest.getMessage());
        assertEquals(Author.ASSISTANT, capturedRequest.getAuthor());
    }
}

