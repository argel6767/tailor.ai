package com.argel6767.tailor.ai.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3Service s3Service;

    private final String BUCKET_NAME = "test-bucket";

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        s3Service.setBucket(BUCKET_NAME);
    }


    @Test
    void testUploadFileSuccessfully() throws IOException {
        // Given
        Long sess = 123434L;
        File testFile = createTempFile();

        // When
        s3Service.uploadFile(sess, testFile);

        // Then
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    private File createTempFile() throws IOException {
        Path tempFile = tempDir.resolve("test.pdf");
        Files.write(tempFile, "test content".getBytes());
        return tempFile.toFile();
    }

    @Test
    void testDownloadFileReturnsCorrectResponseEntity() {
        // Given
        String key = "test-key";
        byte[] dummyData = "test pdf content".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(dummyData);
        ResponseInputStream<GetObjectResponse> responseInputStream =
                new ResponseInputStream<>(GetObjectResponse.builder().build(), inputStream);

        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(responseInputStream);

        // When
        ResponseEntity<InputStreamResource> response = s3Service.downloadFile(key);

        // Then
        assertNotNull(response);
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getContentDisposition().toString()
                .contains("inline; filename=\"test-key.pdf\""));
        assertNotNull(response.getBody());
    }

    @Test
    void testSetAndGetBucket() {
        // Given
        String newBucket = "new-bucket";

        // When
        s3Service.setBucket(newBucket);

        // Then
        assertEquals(newBucket, s3Service.getBucket());
    }

    @Test
    void testSetAndGetS3Client() {
        // Given
        S3Client newClient = mock(S3Client.class);

        // When
        s3Service.setS3client(newClient);

        // Then
        assertEquals(newClient, s3Service.getS3client());
    }

    @Test
    void testUploadFileValidatesParameters() {
        // Given
        Long key = null;
        File mockFile = mock(File.class);

        // Then
        assertThrows(NullPointerException.class, () -> s3Service.uploadFile(key, mockFile));
    }

    @Test
    void testDownloadFileValidatesParameters() {
        // Given
        String key = null;
        // Then
        assertThrows(IllegalArgumentException.class, () -> s3Service.downloadFile(key));
    }
}