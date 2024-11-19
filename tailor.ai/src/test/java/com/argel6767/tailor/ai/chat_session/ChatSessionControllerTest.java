package com.argel6767.tailor.ai.chat_session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        mockChatSession.setChatSessionId(TEST_CHAT_SESSION_ID);
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
        mockMvc.perform(multipart("/chatsession/{email}", TEST_EMAIL)
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatSessionId").value(TEST_CHAT_SESSION_ID))
                .andExpect(jsonPath("$.s3FileKey").value("test-key"));

        verify(chatSessionService).createChatSession(any(MultipartFile.class), eq(TEST_EMAIL));
    }

    @Test
    void testCreateChatSessionShouldHandleMissingFile() throws Exception {
        // Act & Assert
        mockMvc.perform(multipart("/chatsession/{email}", TEST_EMAIL))
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
        mockMvc.perform(multipart("/chatsession/")
                        .file(file))
                .andExpect(status().isNotFound());

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

        when(chatSessionService.createChatSession(any(MultipartFile.class), eq(TEST_EMAIL)))
                .thenReturn(mockChatSession);

        // Act & Assert
        mockMvc.perform(multipart("/chatsession/{email}", TEST_EMAIL)
                        .file(largeFile))
                .andExpect(status().isOk());

        verify(chatSessionService).createChatSession(any(MultipartFile.class), eq(TEST_EMAIL));
    }

    @Test
    void testGetUserChatSessionsWithEmptyList() throws Exception {
        // Arrange
        when(chatSessionService.getAllUserChatSessions(TEST_EMAIL))
                .thenReturn(ResponseEntity.ok(List.of()));

        // Act & Assert
        mockMvc.perform(get("/chatsession/all/{email}", TEST_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(chatSessionService).getAllUserChatSessions(TEST_EMAIL);
    }

    @Test
    void testGetChatSessionPDFReturnsSuccess() throws Exception {
        // Arrange
        byte[] pdfContentBytes = "Test PDF Content".getBytes();
        ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfContentBytes);
        InputStreamResource pdfContent = new InputStreamResource(pdfInputStream);

        ResponseEntity<?> responseEntity = ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);

        Mockito.<ResponseEntity<?>>when(chatSessionService.getChatSessionPDF(TEST_CHAT_SESSION_ID))
                .thenReturn(responseEntity);

        // Act & Assert
        mockMvc.perform(get("/chatsession/pdf/{id}", TEST_CHAT_SESSION_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(pdfContentBytes));

        verify(chatSessionService).getChatSessionPDF(TEST_CHAT_SESSION_ID);
    }

    @Test
    void testGetChatSessionPDFReturnsNotFound() throws Exception {
        // Arrange
        when(chatSessionService.getChatSessionPDF(TEST_CHAT_SESSION_ID))
                .thenReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(get("/chatsession/pdf/{id}", TEST_CHAT_SESSION_ID))
                .andExpect(status().isNotFound());

        verify(chatSessionService).getChatSessionPDF(TEST_CHAT_SESSION_ID);
    }

    @Test
    void testGetUserChatSessionsWithInvalidEmail() throws Exception {
        // Arrange
        String invalidEmail = "invalid-email";
        when(chatSessionService.getAllUserChatSessions(invalidEmail))
                .thenReturn(ResponseEntity.badRequest().build());

        // Act & Assert
        mockMvc.perform(get("/chatsession/all/{email}", invalidEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(chatSessionService).getAllUserChatSessions(invalidEmail);
    }

    @Test
    void testGetChatSession() throws Exception {
        //Arrange
        when(chatSessionService.getChatSession(TEST_CHAT_SESSION_ID)).thenReturn(mockChatSession);

        //Act & Assert
        mockMvc.perform(get("/chatsession/{id}", TEST_CHAT_SESSION_ID)).andExpect(status().isOk());

        verify(chatSessionService).getChatSession(TEST_CHAT_SESSION_ID);
    }

    @Test
    void testGetChatSessionWithInvalidId() throws Exception {
        //Arrange
        when(chatSessionService.getChatSession(TEST_CHAT_SESSION_ID)).thenReturn(null);

        //Act & Assert
        mockMvc.perform(get("/chatsession/{id}", TEST_CHAT_SESSION_ID)).andExpect(status().isNotFound());

        verify(chatSessionService).getChatSession(TEST_CHAT_SESSION_ID);
    }

    @Test
    void testUpdateChatSessionName() throws Exception {
        //Arrange
        String name =  "New name";
        when(chatSessionService.updateChatSessionName(TEST_CHAT_SESSION_ID, name)).thenReturn(ResponseEntity.ok().build());
        //Act & Assert
        mockMvc.perform(put("/chatsession/{id}/name", TEST_CHAT_SESSION_ID)
                .content(name).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        verify(chatSessionService).updateChatSessionName(TEST_CHAT_SESSION_ID, name);
    }

    @Test
    void testUpdateChatSessionNameWithInvalidId() throws Exception {
        String name =  "New name";
        when(chatSessionService.updateChatSessionName(TEST_CHAT_SESSION_ID, name)).thenReturn(ResponseEntity.notFound().build());

        //Act & Assert
        mockMvc.perform(put("/chatsession/{id}/name", TEST_CHAT_SESSION_ID)
                .content(name).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

        verify(chatSessionService).updateChatSessionName(TEST_CHAT_SESSION_ID, name);

    }
}
