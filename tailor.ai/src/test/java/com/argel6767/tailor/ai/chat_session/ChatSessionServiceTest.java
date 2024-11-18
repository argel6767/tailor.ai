package com.argel6767.tailor.ai.chat_session;

import com.argel6767.tailor.ai.message.MessageService;
import com.argel6767.tailor.ai.s3.S3Service;
import com.argel6767.tailor.ai.user.User;
import com.argel6767.tailor.ai.user.UserRepository;
import com.argel6767.tailor.ai.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChatSessionServiceTest {

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @Mock
    private MessageService messageService;

    @Mock
    private UserService userService;

    @Mock
    private S3Service s3Service;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatSessionService chatSessionService;

    private ChatSessionPage chatSession;
    private Long chatSessionId;
    private User user;
    private MultipartFile mockPdfFile;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_S3_KEY = "test-file-key";

    @BeforeEach
    void setUp() {
        chatSession = new ChatSessionPage();
        chatSession.setChatSessionId(chatSessionId);

        user = new User();
        user.setEmail(TEST_EMAIL);
        List<ChatSessionPage> chatSessions = new ArrayList<>();
        chatSessions.add(chatSession);
        user.setChatSessions(chatSessions);

        mockPdfFile = mock(MultipartFile.class);
    }

    @Test
    void testAddChatSessionShouldSaveAndReturnChatSession() {
        // Arrange
        when(chatSessionRepository.save(any(ChatSessionPage.class))).thenReturn(chatSession);

        // Act
        ChatSessionPage result = chatSessionService.addChatSession(chatSession);

        // Assert
        assertNotNull(result);
        assertEquals(chatSession.getChatSessionId(), result.getChatSessionId());
        verify(chatSessionRepository).save(chatSession);
    }

    @Test
    void testCreateChatSessionShouldCreateSessionWithFileAndUser() {
        // Arrange
        when(chatSessionRepository.save(any(ChatSessionPage.class))).thenReturn(chatSession);
        when(userService.getUserByEmail(TEST_EMAIL)).thenReturn(user);
        when(s3Service.uploadFile(any(), any(File.class))).thenReturn(TEST_S3_KEY);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ChatSessionPage result = chatSessionService.createChatSession(mockPdfFile, TEST_EMAIL);

        // Verify interactions
        verify(chatSessionRepository, times(2)).save(any(ChatSessionPage.class));
        verify(s3Service).uploadFile(any(), any());
        verify(userService).getUserByEmail(TEST_EMAIL);
        verify(userRepository).save(user);
    }

    @Test
    void testCreateChatSessionShouldHandleUserNotFound() {
        // Arrange
        when(userService.getUserByEmail(TEST_EMAIL)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> chatSessionService.createChatSession(mockPdfFile, TEST_EMAIL));

        verify(s3Service, never()).uploadFile(any(), any(File.class));
    }

    @Test
    void testCreateChatSessionShouldHandleS3UploadFailure() {
        // Arrange
        when(chatSessionRepository.save(any(ChatSessionPage.class))).thenReturn(chatSession);
        when(userService.getUserByEmail(TEST_EMAIL)).thenReturn(user);
        when(s3Service.uploadFile(any(), any(File.class))).thenThrow(new RuntimeException("Upload failed"));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> chatSessionService.createChatSession(mockPdfFile, TEST_EMAIL));
    }

    @Test
    void testLinkUserToChatSessionShouldProperlyLinkUserAndSession() {
        // Arrange
        when(chatSessionRepository.save(any(ChatSessionPage.class))).thenReturn(chatSession);
        when(userService.getUserByEmail(TEST_EMAIL)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        chatSessionService.createChatSession(mockPdfFile, TEST_EMAIL);

        // Assert
        verify(userRepository).save(user);
    }

    @Test
    void testCreateChatSessionShouldHandleNullFile() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> chatSessionService.createChatSession(null, TEST_EMAIL));

        verify(s3Service, never()).uploadFile(any(), any());
    }

    @Test
    void testCreateChatSessionShouldHandleNullEmail() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> chatSessionService.createChatSession(mockPdfFile, null));

        verify(s3Service, never()).uploadFile(any(), any());
    }

    @Test
    void testGetAllUserChatSessionsReturnsUserChatSessions() {
        // Arrange
        when(userService.getUserByEmail(TEST_EMAIL)).thenReturn(user);

        // Act
        ResponseEntity<List<ChatSessionPage>> response = chatSessionService.getAllUserChatSessions(TEST_EMAIL);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(chatSessionId, response.getBody().get(0).getChatSessionId());

        verify(userService).getUserByEmail(TEST_EMAIL);
    }

    @Test
    void testGetAllUserChatSessionsWithEmptyChatSessions() {
        // Arrange
        user.setChatSessions(new ArrayList<>());
        when(userService.getUserByEmail(TEST_EMAIL)).thenReturn(user);

        // Act
        ResponseEntity<List<ChatSessionPage>> response = chatSessionService.getAllUserChatSessions(TEST_EMAIL);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(userService).getUserByEmail(TEST_EMAIL);
    }

    @Test
    void testGetChatSessionPDFReturnsSuccessfulResponse() {
        // Arrange
        ChatSessionPage session = new ChatSessionPage();
        session.setChatSessionId(chatSessionId);
        session.setS3FileKey(TEST_S3_KEY);  // Explicitly set the S3 key

        when(chatSessionRepository.findById(chatSessionId)).thenReturn(Optional.of(session));
        when(s3Service.downloadFile(TEST_S3_KEY)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> response = chatSessionService.getChatSessionPDF(chatSessionId);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(chatSessionRepository).findById(chatSessionId);
        verify(s3Service).downloadFile(TEST_S3_KEY);
    }

    @Test
    void testGetChatSessionPDFWithNonExistentSession() {
        // Arrange
        when(chatSessionRepository.findById(chatSessionId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = chatSessionService.getChatSessionPDF(chatSessionId);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        verify(chatSessionRepository).findById(chatSessionId);
        verify(s3Service, never()).downloadFile(anyString());
    }
}