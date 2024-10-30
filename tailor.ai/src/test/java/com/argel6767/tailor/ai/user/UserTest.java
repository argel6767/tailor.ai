package com.argel6767.tailor.ai.user;
import com.argel6767.tailor.ai.chat_session.ChatSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void testSetAndGetId() {
        Long id = 1L;
        user.setId(id);
        assertEquals(id, user.getId(), "User ID should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetEmail() {
        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail(), "Email should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetIsEmailVerified() {
        user.setIsEmailVerified(true);
        assertTrue(user.getIsEmailVerified(), "isEmailVerified should be true after setting to true.");
    }

    @Test
    public void testSetAndGetPasswordHash() {
        String passwordHash = "hashed_password";
        user.setPasswordHash(passwordHash);
        assertEquals(passwordHash, user.getPasswordHash(), "Password hash should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetProfession() {
        String profession = "Engineer";
        user.setProfession(profession);
        assertEquals(profession, user.getProfession(), "Profession should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        user.setCreatedAt(createdAt);
        assertEquals(createdAt, user.getCreatedAt(), "CreatedAt should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetLastLogin() {
        LocalDateTime lastLogin = LocalDateTime.now();
        user.setLastLogin(lastLogin);
        assertEquals(lastLogin, user.getLastLogin(), "LastLogin should be set and retrieved correctly.");
    }

    @Test
    public void testSetAndGetChatSessions() {
        List<ChatSession> chatSessions = new ArrayList<>();
        ChatSession chatSession = new ChatSession();
        chatSessions.add(chatSession);
        user.setChatSessions(chatSessions);
        assertEquals(chatSessions, user.getChatSessions(), "ChatSessions should be set and retrieved correctly.");
    }
}

