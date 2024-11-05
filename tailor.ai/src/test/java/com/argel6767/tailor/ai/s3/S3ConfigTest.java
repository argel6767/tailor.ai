package com.argel6767.tailor.ai.s3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class S3ConfigTest {

    @InjectMocks
    private S3Config s3Config;

    @Test
    void testS3ClientShouldCreateClientWithConfiguredRegion() {
        // Arrange
        String expectedRegion = "us-east-1";
        ReflectionTestUtils.setField(s3Config, "region", expectedRegion);

        // Act
        S3Client client = s3Config.s3Client();

        // Assert
        assertNotNull(client, "S3Client should not be null");
        assertEquals(Region.of(expectedRegion), client.serviceClientConfiguration().region(),
                "S3Client should be configured with the correct region");
    }

}