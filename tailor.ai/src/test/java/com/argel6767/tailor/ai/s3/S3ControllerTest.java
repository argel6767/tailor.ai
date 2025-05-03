package com.argel6767.tailor.ai.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3ControllerTest {

    @Mock
    private S3Service s3Service;
    @Mock
    private MultipartFile multipartFile;
    private S3Controller s3Controller;
    private final Long TEST_CHAT_SESSION_ID = 1L;
    private final String TEST_USER_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        s3Controller = new S3Controller(s3Service);
    }

    @Test
    void testUploadPDF_Success() {
        // Arrange
        when(s3Service.uploadFile(multipartFile, TEST_CHAT_SESSION_ID)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> response = s3Controller.uploadPDF(multipartFile, TEST_CHAT_SESSION_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(s3Service).uploadFile(multipartFile, TEST_CHAT_SESSION_ID);
    }

    @Test
    void testUploadPDF_NotFound() {
        // Arrange
        ResponseEntity<?> expectedResponse = ResponseEntity.notFound().build();
        when(s3Service.uploadFile(multipartFile, TEST_CHAT_SESSION_ID)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        // Act
        ResponseEntity<?> response = s3Controller.uploadPDF(multipartFile, TEST_CHAT_SESSION_ID);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(s3Service).uploadFile(multipartFile, TEST_CHAT_SESSION_ID);
    }

    @Test
    void testDownloadPDF_Success() {
        // Arrange
        when(s3Service.getFile(TEST_CHAT_SESSION_ID)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> response = s3Controller.downloadPDF(TEST_CHAT_SESSION_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(s3Service).getFile(TEST_CHAT_SESSION_ID);
    }

    @Test
    void testDownloadPDF_NotFound() {
        // Arrange
        when(s3Service.getFile(TEST_CHAT_SESSION_ID)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        // Act
        ResponseEntity<?> response = s3Controller.downloadPDF(TEST_CHAT_SESSION_ID);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(s3Service).getFile(TEST_CHAT_SESSION_ID);
    }

    @Test
    void testDeletePDF_Success() {
        // Arrange
        when(s3Service.deleteFile(TEST_CHAT_SESSION_ID)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> response = s3Controller.deletePDF(TEST_CHAT_SESSION_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(s3Service).deleteFile(TEST_CHAT_SESSION_ID);
    }

    @Test
    void testDeletePDF_NotFound() {
        // Arrange
        when(s3Service.deleteFile(TEST_CHAT_SESSION_ID)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        // Act
        ResponseEntity<?> response = s3Controller.deletePDF(TEST_CHAT_SESSION_ID);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(s3Service).deleteFile(TEST_CHAT_SESSION_ID);
    }

    @Test
    void testDeleteAllUserFiles_Success() {
        // Arrange
        when(s3Service.deleteAllFiles(TEST_USER_EMAIL)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> response = s3Controller.deleteAllUserFiles();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(s3Service).deleteAllFiles(TEST_USER_EMAIL);
    }

    @Test
    void testDeleteAllUserFiles_NotFound() {
        // Arrange
        when(s3Service.deleteAllFiles(TEST_USER_EMAIL)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        // Act
        ResponseEntity<?> response = s3Controller.deleteAllUserFiles();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(s3Service).deleteAllFiles(TEST_USER_EMAIL);
    }
}