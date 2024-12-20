package com.argel6767.tailor.ai.s3;

import com.argel6767.tailor.ai.chat_session.ChatSession;
import com.argel6767.tailor.ai.chat_session.ChatSessionService;
import com.argel6767.tailor.ai.user.User;
import com.argel6767.tailor.ai.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private S3Client s3Client;
    @Mock
    private UserService userService;
    @Mock
    private ChatSessionService chatSessionService;
    @Mock
    private MultipartFile multipartFile;

    private S3Service s3Service;
    private final String BUCKET_NAME = "test-bucket";

    @BeforeEach
    void setUp() {
        s3Service = new S3Service(s3Client, userService, chatSessionService);
        s3Service.setBucket(BUCKET_NAME);
    }

    @Test
    void testUploadFile_Success() {
        // Arrange
        Long chatSessionId = 1L;
        ChatSession chatSession = new ChatSession();

        when(chatSessionService.getChatSession(chatSessionId)).thenReturn(chatSession);
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);

        // Act
        ResponseEntity<?> response = s3Service.uploadFile(multipartFile, chatSessionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(chatSessionService).saveChatSession(chatSession);
        assertNotNull(chatSession.getS3FileKey());
    }

    @Test
    void testUploadFile_ChatSessionNotFound() {
        // Arrange
        Long chatSessionId = 1L;
        when(chatSessionService.getChatSession(chatSessionId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = s3Service.uploadFile(multipartFile, chatSessionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetFile_Success() {
        // Arrange
        Long chatSessionId = 1L;
        ChatSession chatSession = new ChatSession();
        chatSession.setS3FileKey("test-key");

        when(chatSessionService.getChatSession(chatSessionId)).thenReturn(chatSession);
        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenReturn(any());

        // Act
        ResponseEntity<?> response = s3Service.getFile(chatSessionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof InputStreamResource);
    }

    @Test
    void testGetFile_NotFound() {
        // Arrange
        Long chatSessionId = 1L;
        when(chatSessionService.getChatSession(chatSessionId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = s3Service.getFile(chatSessionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetFile_S3KeyNotFound() {
        // Arrange
        Long chatSessionId = 1L;
        ChatSession chatSession = new ChatSession();
        chatSession.setS3FileKey("non-existent-key");

        when(chatSessionService.getChatSession(chatSessionId)).thenReturn(chatSession);
        when(s3Client.getObject(any(GetObjectRequest.class))).thenThrow(NoSuchKeyException.class);

        // Act
        ResponseEntity<?> response = s3Service.getFile(chatSessionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteFile_Success() {
        // Arrange
        Long chatSessionId = 1L;
        ChatSession chatSession = new ChatSession();
        chatSession.setS3FileKey("test-key");

        when(chatSessionService.getChatSession(chatSessionId)).thenReturn(chatSession);

        // Act
        ResponseEntity<?> response = s3Service.deleteFile(chatSessionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void testDeleteFile_NotFound() {
        // Arrange
        Long chatSessionId = 1L;
        when(chatSessionService.getChatSession(chatSessionId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = s3Service.deleteFile(chatSessionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteAllFiles_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        List<ChatSession> chatSessions = new ArrayList<>();
        ChatSession chatSession = new ChatSession();
        chatSession.setS3FileKey("test-key");
        chatSessions.add(chatSession);
        user.setChatSessions(chatSessions);

        when(userService.getUserByEmail(userEmail)).thenReturn(user);

        // Act
        ResponseEntity<?> response = s3Service.deleteAllFiles(userEmail);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(s3Client).deleteObjects(any(DeleteObjectsRequest.class));
    }

    @Test
    void testDeleteAllFiles_UserNotFound() {
        // Arrange
        String userEmail = "nonexistent@example.com";
        when(userService.getUserByEmail(userEmail)).thenThrow(UsernameNotFoundException.class);

        // Act
        ResponseEntity<?> response = s3Service.deleteAllFiles(userEmail);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteAllFiles_S3Error() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        List<ChatSession> chatSessions = new ArrayList<>();
        ChatSession chatSession = new ChatSession();
        chatSession.setS3FileKey("test-key");
        chatSessions.add(chatSession);
        user.setChatSessions(chatSessions);

        when(userService.getUserByEmail(userEmail)).thenReturn(user);
        when(s3Client.deleteObjects(any(DeleteObjectsRequest.class))).thenThrow(SdkException.class);

        // Act
        ResponseEntity<?> response = s3Service.deleteAllFiles(userEmail);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}