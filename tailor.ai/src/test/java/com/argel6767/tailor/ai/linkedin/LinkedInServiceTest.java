package com.argel6767.tailor.ai.linkedin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LinkedInServiceTest {

    private LinkedInService linkedInService;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setUp() {
        linkedInService = new LinkedInService(restClient);

        // Set endpoint using reflection for testing
        ReflectionTestUtils.setField(linkedInService, "endpoint", "/test-endpoint");
    }

    @Test
    public void testGetJobDetails_Successful() {
        // Prepare mock data
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("job_description", "Software Engineer role");
        mockResponse.put("skills", Arrays.asList("Java", "Spring", "REST"));

        // Setup mock behavior
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Map.class)).thenReturn(mockResponse);

        // Invoke method
        String result = linkedInService.getJobDetails("https://example.com/job");

        // Verify interactions
        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(eq("/test-endpoint"));

        // Assert result
        assertTrue(result.contains("Software Engineer role"));
        assertTrue(result.contains("Java"));
        assertTrue(result.contains("Spring"));
        assertTrue(result.contains("REST"));
    }

    @Test
    public void testGetJobDetails_ExceptionHandling() {
        // Setup mock to throw an exception
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Map.class)).thenThrow(new RuntimeException("API Error"));

        // Invoke method
        String result = linkedInService.getJobDetails("https://example.com/job");

        // Assert empty string is returned on exception
        assertEquals("", result);
    }

    @Test
    public void testGetterAndSetter() {
        // Test getter
        assertEquals("/test-endpoint", linkedInService.getEndpoint());

        // Test setter
        linkedInService.setEndpoint("/new-endpoint");
        assertEquals("/new-endpoint", linkedInService.getEndpoint());
    }
}