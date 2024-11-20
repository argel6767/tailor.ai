package com.argel6767.tailor.ai.openai;

import com.argel6767.tailor.ai.message.Author;
import com.argel6767.tailor.ai.message.Message;
import com.argel6767.tailor.ai.message.MessageService;
import com.argel6767.tailor.ai.message.requests.NewMessageRequest;
import com.argel6767.tailor.ai.message.responses.AiResponse;
import com.argel6767.tailor.ai.pdf.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OpenAiServiceTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private MessageService messageService;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private OpenAiService openAiService;

    private final String AI_RESPONSE = "AI RESPONSE";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAiResponse_callsChatClientAndSavesMessage() {
        // Arrange
        Long chatSessionId = 1L;
        String userMessage = "Hello AI";

        // Mock message history
        when(messageService.getAllChatSessionMessages(chatSessionId))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        // Mock chatClient to return a fixed response

        // Act
        ResponseEntity<AiResponse> response = openAiService.getAiResponse(chatSessionId, userMessage);

        // Assert
        assertNotNull(response);

        // Verify that chatClient.prompt was called with any string
        verify(chatClient).prompt(anyString());

        // Verify that messageService.createMessage was called with expected arguments
        verify(messageService).createMessage(
                argThat(request -> request.getMessage().equals("AI Response") && request.getAuthor() == Author.ASSISTANT),
                eq(chatSessionId)
        );
    }

    @Test
    public void testSendPDFForReading_callsPdfServiceAndChatClient() throws IOException {
        // Arrange
        Long chatSessionId = 1L;
        String profession = "Software Engineer";
        MultipartFile multipartFile = mock(MultipartFile.class);
        String fileContent = "Resume Content";

        // Mock pdfService to return fileContent
        when(pdfService.readFile(any(File.class))).thenReturn(fileContent);

        // Mock chatClient to return a fixed response


        // Act
        ResponseEntity<AiResponse> response = openAiService.sendPDFForReading(multipartFile, profession, chatSessionId);


        // Verify that pdfService.readFile was called
        verify(pdfService).readFile(any(File.class));

        // Verify that chatClient.prompt was called with any string
        verify(chatClient).prompt(anyString());

        // Verify that messageService.createMessage was called with expected arguments
        verify(messageService).createMessage(
                argThat(request -> request.getMessage().equals("Tailored Resume") && request.getAuthor() == Author.ASSISTANT),
                eq(chatSessionId)
        );
    }

    @Test
    public void testSendPDFForReading_handlesIOException() throws IOException {
        // Arrange
        Long chatSessionId = 1L;
        String profession = "Software Engineer";
        MultipartFile multipartFile = mock(MultipartFile.class);

        // Mock pdfService.readFile to throw IOException
        when(pdfService.readFile(any(File.class))).thenThrow(new IOException("Failed to read file"));

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> {
            openAiService.sendPDFForReading(multipartFile, profession, chatSessionId);
        });

        assertEquals("Failed to read file", exception.getMessage());

        // Verify that pdfService.readFile was called
        verify(pdfService).readFile(any(File.class));

        // Verify that chatClient.prompt was not called
        verify(chatClient, never()).prompt(anyString());

        // Verify that messageService.createMessage was not called
        verify(messageService, never()).createMessage(any(), anyLong());
    }
}


