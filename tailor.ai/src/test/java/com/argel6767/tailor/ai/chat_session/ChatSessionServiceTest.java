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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

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

    private ChatSession chatSession;
    private User user;
    private File mockPdfFile;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_S3_KEY = "test-file-key";

    @BeforeEach
    void setUp() {
        chatSession = new ChatSession();
        chatSession.setChatSessionId(1L);

        user = new User();
        user.setEmail(TEST_EMAIL);
        user.setChatSessions(new ArrayList<>());

        mockPdfFile = mock(File.class);
    }

    @Test
    void testAddChatSessionShouldSaveAndReturnChatSession() {
        // Arrange
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(chatSession);

        // Act
        ChatSession result = chatSessionService.addChatSession(chatSession);

        // Assert
        assertNotNull(result);
        assertEquals(chatSession.getChatSessionId(), result.getChatSessionId());
        verify(chatSessionRepository).save(chatSession);
    }

    @Test
    void testCreateChatSessionShouldCreateSessionWithFileAndUser() {
        // Arrange
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(chatSession);
        when(userService.getUserByEmail(TEST_EMAIL)).thenReturn(user);
        when(s3Service.uploadFile(any(), any(File.class))).thenReturn(TEST_S3_KEY);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ChatSession result = chatSessionService.createChatSession(mockPdfFile, TEST_EMAIL);

        // Verify interactions
        verify(chatSessionRepository, times(2)).save(any(ChatSession.class));
        verify(s3Service).uploadFile(any(), eq(mockPdfFile));
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
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(chatSession);
        when(userService.getUserByEmail(TEST_EMAIL)).thenReturn(user);
        when(s3Service.uploadFile(any(), any(File.class))).thenThrow(new RuntimeException("Upload failed"));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> chatSessionService.createChatSession(mockPdfFile, TEST_EMAIL));
    }

    @Test
    void testLinkUserToChatSessionShouldProperlyLinkUserAndSession() {
        // Arrange
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(chatSession);
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
}