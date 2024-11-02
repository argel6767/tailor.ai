package com.argel6767.tailor.ai.email;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EmailConfigurationTest {

    private final String EMAIL_ADDRESS = "<EMAIL>";
    private final String EMAIL_PASSWORD = "<PASSWORD>";
    private EmailConfiguration emailConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailConfiguration = new EmailConfiguration();

    }
    @Test
    void javaMailSender() {

        emailConfiguration.javaMailSender();
    }
}