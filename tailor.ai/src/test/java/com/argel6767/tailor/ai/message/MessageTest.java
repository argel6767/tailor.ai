package com.argel6767.tailor.ai.message;

import com.argel6767.tailor.ai.chat_session.ChatSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTest {

    private Message message;

    @BeforeEach
    public void setUp() {
        message = new Message();
    }

    @Test
    public void testSetAndGetMessageId() {
        Long id = 1L;
        message.setMessageId(id);
        assertEquals(id, message.getMessageId(), "Message ID should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetAuthor() {
        Author author = Author.USER;
        message.setAuthor(author);
        assertEquals(author, message.getAuthor(), "Author should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetBody() {
        String body = "This is a test message.";
        message.setBody(body);
        assertEquals(body, message.getBody(), "Body should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        message.setCreatedAt(createdAt);
        assertEquals(createdAt, message.getCreatedAt(), "CreatedAt should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetChatSession() {
        ChatSession chatSession = new ChatSession();
        message.setChatSession(chatSession);
        assertEquals(chatSession, message.getChatSession(), "ChatSession should be set and retrieved correctly.");
    }
}

