package com.argel6767.tailor.ai.chat_session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.argel6767.tailor.ai.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


public class ChatSessionTest {

    @Test
    @DisplayName("Test setting creation time with null")
    public void testSetCreatedAtWithNull() {
        ChatSession chatSession = new ChatSession();
        chatSession.setCreatedAt(null);

        LocalDateTime result = chatSession.getCreatedAt();

        assertNull(result);
    }

    @Test
    @DisplayName("Test setting creation time with a specific date")
    public void testSetCreatedAtWithSpecificDate() {
        LocalDateTime now = LocalDateTime.now();
        ChatSession chatSession = new ChatSession();
        chatSession.setCreatedAt(now);

        LocalDateTime result = chatSession.getCreatedAt();

        assertEquals(now, result);
    }

    @Test
    @DisplayName("Test setting creation time with past date")
    public void testSetCreatedAtWithPastDate() {
        LocalDateTime past = LocalDateTime.now().minusDays(5);
        ChatSession chatSession = new ChatSession();
        chatSession.setCreatedAt(past);

        LocalDateTime result = chatSession.getCreatedAt();

        assertEquals(past, result);
    }

    @Test
    @DisplayName("Test setting creation time with future date")
    public void testSetCreatedAtWithFutureDate() {
        LocalDateTime future = LocalDateTime.now().plusDays(5);
        ChatSession chatSession = new ChatSession();
        chatSession.setCreatedAt(future);

        LocalDateTime result = chatSession.getCreatedAt();

        assertEquals(future, result);
    }

    @Test
    @DisplayName("Test setting creation time and changing it")
    public void testSetCreatedAtAndChange() {
        LocalDateTime initial = LocalDateTime.now().minusDays(1);
        LocalDateTime changed = LocalDateTime.now().plusDays(1);

        ChatSession chatSession = new ChatSession();
        chatSession.setCreatedAt(initial);
        chatSession.setCreatedAt(changed);

        LocalDateTime result = chatSession.getCreatedAt();

        assertNotEquals(initial, result);
        assertEquals(changed, result);
    }
}