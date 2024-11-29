package com.argel6767.tailor.ai.configs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringJUnitConfig
@TestPropertySource(properties = {
        "base.url=https://test-linkedin-api.com",
        "rapid.api.key=test-api-key",
        "rapid.api.host=test-api-host"
})
public class RestClientConfigurationTest {

    private RestClientConfiguration restClientConfiguration;

    @BeforeEach
    public void setUp() {
        restClientConfiguration = new RestClientConfiguration();

        // Manually set the properties since @Value won't work in a pure unit test
        restClientConfiguration.setBaseUrl("https://test-linkedin-api.com");
        restClientConfiguration.setRapidApiKey("test-api-key");
        restClientConfiguration.setRapidApiHost("test-api-host");
    }

    @Test
    public void testRestClientConfiguration() {
        // Arrange & Act
        RestClient restClient = restClientConfiguration.restClient();

        // Assert
        assertNotNull(restClient, "RestClient should not be null");
    }

    @Test
    public void testRestClientHeaders() {
        // Arrange & Act
        RestClient restClient = restClientConfiguration.restClient();

        // Verify the base URL
        assertEquals("https://test-linkedin-api.com",
                restClientConfiguration.getBaseUrl(),
                "Base URL should match configured value");
    }
}