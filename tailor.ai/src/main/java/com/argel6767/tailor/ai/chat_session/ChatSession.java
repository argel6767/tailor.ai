package com.argel6767.tailor.ai.chat_session;

import com.argel6767.tailor.ai.message.Message;
import com.argel6767.tailor.ai.user.User;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long chatSessionId;

    @Column(nullable = false, columnDefinition = "varchar(100) default 'New Chat'")
    private String chatSessionName;

    private String s3FileUrl;
    
    @Timestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "chatSession")
    private List<Message> messages;
    
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

    public String getS3FileUrl() {
        return s3FileUrl;
    }

    public void setS3FileUrl(String s3FileUrl) {
        this.s3FileUrl = s3FileUrl;
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
