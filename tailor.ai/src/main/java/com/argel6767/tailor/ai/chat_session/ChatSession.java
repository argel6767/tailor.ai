package com.argel6767.tailor.ai.chat_session;

import com.argel6767.tailor.ai.user.User;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;

import java.time.LocalDateTime;

@Entity
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long chatSessionId;

    @Column(nullable = false, columnDefinition = "varchar(100) default 'New Chat'")
    private String chatSessionName;
    
    @Timestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    public void setChatSessionId(Long chatSessionId) {
        this.chatSessionId = chatSessionId;
    }

    public Long getChatSessionId() {
        return chatSessionId;
    }

    public String getChatSessionName() {
        return chatSessionName;
    }

    public void setChatSessionName(String chatSessionName) {
        this.chatSessionName = chatSessionName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
