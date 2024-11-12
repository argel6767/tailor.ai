package com.argel6767.tailor.ai.chat_session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class ChatSessionControllerTest {

    @Mock
    private ChatSessionService chatSessionService;

    @InjectMocks
    private ChatSessionController chatSessionController;

    private MockMvc mockMvc;
    private ChatSession mockChatSession;
    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_CHAT_SESSION_ID = 1L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(chatSessionController)
                .build();

        mockChatSession = new ChatSession();
        mockChatSession.setChatSessionId(1L);
        mockChatSession.setS3FileKey("test-key");
    }

    @Test
    void testCreateChatSessionShouldReturnCreatedChatSession() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "test pdf content".getBytes()
        );

        when(chatSessionService.createChatSession(any(MultipartFile.class), eq(TEST_EMAIL)))
                .thenReturn(mockChatSession);

        // Act & Assert
        mockMvc.perform(multipart("/chatsession/session")
                        .file(file)
                        .content(TEST_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatSessionId").value(mockChatSession.getChatSessionId()))
                .andExpect(jsonPath("$.s3FileKey").value(mockChatSession.getS3FileKey()));

        verify(chatSessionService).createChatSession(any(MultipartFile.class), eq(TEST_EMAIL));
    }

    @Test
    void testCreateChatSessionShouldHandleMissingFile() throws Exception {
        // Act & Assert
        mockMvc.perform(multipart("/chatsession/session")
                        .content(TEST_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(chatSessionService, never()).createChatSession(any(), any());
    }

    @Test
    void testCreateChatSessionShouldHandleMissingEmail() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "test pdf content".getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/chatsession/session")
                        .file(file))
                .andExpect(status().isBadRequest());

        verify(chatSessionService, never()).createChatSession(any(), any());
    }


    @Test
    void testCreateChatSessionShouldHandleLargeFile() throws Exception {
        // Arrange
        byte[] largeContent = new byte[5 * 1024 * 1024]; // 5MB file
        MockMultipartFile largeFile = new MockMultipartFile(
                "file",
                "large.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                largeContent
        );

        // Act & Assert
        mockMvc.perform(multipart("/chatsession/session")
                        .file(largeFile)
                        .content(TEST_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(chatSessionService).createChatSession(any(MultipartFile.class), eq(TEST_EMAIL));
    }

    @Test
    void testGetUserChatSessionsWithEmptyList() throws Exception {
        // Arrange
        when(chatSessionService.getAllUserChatSessions(TEST_EMAIL))
                .thenReturn(ResponseEntity.ok(List.of()));

        // Act & Assert
        mockMvc.perform(get("/chat/session/{email}", TEST_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetChatSessionPDFReturnsSuccess() throws Exception {
        // Arrange
        byte[] pdfContent = "Test PDF Content".getBytes();
        ResponseEntity<?> responseEntity = ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);

        when(chatSessionService.getChatSessionPDF(TEST_CHAT_SESSION_ID))
                .thenReturn(any());

        // Act & Assert
        mockMvc.perform(get("/chat/session/{id}", TEST_CHAT_SESSION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void testGetChatSessionPDFReturnsNotFound() throws Exception {
        // Arrange
        when(chatSessionService.getChatSessionPDF(TEST_CHAT_SESSION_ID))
                .thenReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(get("/chat/session/{id}", TEST_CHAT_SESSION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserChatSessionsWithInvalidEmail() throws Exception {
        // Arrange
        String invalidEmail = "invalid-email";
        when(chatSessionService.getAllUserChatSessions(invalidEmail))
                .thenReturn(ResponseEntity.badRequest().build());

        // Act & Assert
        mockMvc.perform(get("/chat/session/{email}", invalidEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}