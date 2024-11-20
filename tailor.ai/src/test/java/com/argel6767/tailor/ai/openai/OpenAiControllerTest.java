package com.argel6767.tailor.ai.openai;

import com.argel6767.tailor.ai.message.responses.AiResponse;
import com.argel6767.tailor.ai.openai.requests.AiMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OpenAiControllerTest {

    @Mock
    private OpenAiService openAiService;

    @InjectMocks
    private OpenAiController openAiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAiResponseSuccess() {
        // Arrange
        AiMessageRequest request = new AiMessageRequest();
        request.setChatSessionId(1L);
        request.setUserMessage("Hello AI");
        AiResponse aiResponse = new AiResponse("AI response");

        when(openAiService.getAiResponse(anyLong(), anyString()))
                .thenReturn(ResponseEntity.ok(aiResponse));

        // Act
        ResponseEntity<AiResponse> response = openAiController.getAiResponse(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(aiResponse, response.getBody());

        // Verify service call
        verify(openAiService).getAiResponse(eq(1L), eq("Hello AI"));
    }

    @Test
    void testSubmitFileToAiSuccess() throws IOException {
        // Arrange
        Long chatSessionId = 1L;
        String profession = "Software Engineer";
        MultipartFile file = new MockMultipartFile("file", "resume.pdf", "application/pdf", "Dummy content".getBytes());
        AiResponse aiResponse = new AiResponse("Processed response");

        when(openAiService.sendPDFForReading(any(MultipartFile.class), anyString(), anyLong()))
                .thenReturn(ResponseEntity.ok(aiResponse));

        // Act
        ResponseEntity<AiResponse> response = openAiController.submitFileToAi(file, profession, chatSessionId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(aiResponse, response.getBody());

        // Verify service call
        verify(openAiService).sendPDFForReading(eq(file), eq(profession), eq(chatSessionId));
    }

    @Test
    void testSubmitFileToAiIOException() throws IOException {
        // Arrange
        Long chatSessionId = 1L;
        String profession = "Software Engineer";
        MultipartFile file = new MockMultipartFile("file", "resume.pdf", "application/pdf", "Dummy content".getBytes());

        when(openAiService.sendPDFForReading(any(MultipartFile.class), anyString(), anyLong()))
                .thenThrow(new IOException("File processing error"));

        // Act
        ResponseEntity<AiResponse> response = openAiController.submitFileToAi(file, profession, chatSessionId);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

        // Verify service call
        verify(openAiService).sendPDFForReading(eq(file), eq(profession), eq(chatSessionId));
    }
}
