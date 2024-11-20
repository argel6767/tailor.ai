package com.argel6767.tailor.ai.message.utils;

import com.argel6767.tailor.ai.message.Message;
import com.argel6767.tailor.ai.message.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageHistoryFlattenerTest {

    @Test
    @DisplayName("Flattening an empty message list returns an empty string")
    void testFlattenEmptyMessageHistory() {
        // Arrange
        List<Message> emptyMessages = new ArrayList<>();

        // Act
        String flattenedHistory = MessageHistoryFlattener.flattenMessageHistory(emptyMessages);

        // Assert
        assertEquals("", flattenedHistory);
    }

    @Test
    @DisplayName("Flattening a single message creates correct string representation")
    void testFlattenSingleMessage() {
        // Arrange
        Message message = new Message();
        message.setAuthor(Author.USER);
        message.setBody("Hello, how are you?");

        List<Message> messages = Arrays.asList(message);

        // Act
        String flattenedHistory = MessageHistoryFlattener.flattenMessageHistory(messages);

        // Assert
        assertEquals("USER: Hello, how are you?\n", flattenedHistory);
    }

    @Test
    @DisplayName("Flattening multiple messages creates correct string representation")
    void testFlattenMultipleMessages() {
        // Arrange
        Message message1 = new Message();
        message1.setAuthor(Author.USER);
        message1.setBody("Hello");

        Message message2 = new Message();
        message2.setAuthor(Author.ASSISTANT);
        message2.setBody("Hi there!");

        Message message3 = new Message();
        message3.setAuthor(Author.USER);
        message3.setBody("How are you?");

        List<Message> messages = Arrays.asList(message1, message2, message3);

        // Act
        String flattenedHistory = MessageHistoryFlattener.flattenMessageHistory(messages);

        // Assert
        String expectedHistory = "USER: Hello\n" +
                "ASSISTANT: Hi there!\n" +
                "USER: How are you?\n";
        assertEquals(expectedHistory, flattenedHistory);
    }

    @Test
    @DisplayName("Adding a new user message to flattened history works correctly")
    void testAddNewUserMessage() {
        // Arrange
        String existingHistory = "USER: Previous message\nASSISTANT: Previous response\n";
        String newUserMessage = "What's the next step?";

        // Act
        String updatedHistory = MessageHistoryFlattener.addNewUserMessage(newUserMessage, existingHistory);

        // Assert
        String expectedHistory = "USER: Previous message\n" +
                "ASSISTANT: Previous response\n" +
                "User: What's the next step?\n" +
                "Assistant";
        assertEquals(expectedHistory, updatedHistory);
    }

    @Test
    @DisplayName("Adding a new user message to empty history works correctly")
    void testAddNewUserMessageToEmptyHistory() {
        // Arrange
        String existingHistory = "";
        String newUserMessage = "Hello, start of conversation";

        // Act
        String updatedHistory = MessageHistoryFlattener.addNewUserMessage(newUserMessage, existingHistory);

        // Assert
        assertEquals("User: Hello, start of conversation\nAssistant", updatedHistory);
    }

    @Test
    @DisplayName("Handling messages with special characters")
    void testFlattenMessageWithSpecialCharacters() {
        // Arrange
        Message message = new Message();
        message.setAuthor(Author.USER);
        message.setBody("Hello! @#$%^&* Special chars test");

        List<Message> messages = Arrays.asList(message);

        // Act
        String flattenedHistory = MessageHistoryFlattener.flattenMessageHistory(messages);

        // Assert
        assertEquals("USER: Hello! @#$%^&* Special chars test\n", flattenedHistory);
    }

    @Test
    @DisplayName("Handling messages with newline characters")
    void testFlattenMessageWithNewlines() {
        // Arrange
        Message message = new Message();
        message.setAuthor(Author.USER);
        message.setBody("Multi\nline\nmessage");

        List<Message> messages = Arrays.asList(message);

        // Act
        String flattenedHistory = MessageHistoryFlattener.flattenMessageHistory(messages);

        // Assert
        assertEquals("USER: Multi\nline\nmessage\n", flattenedHistory);
    }
}