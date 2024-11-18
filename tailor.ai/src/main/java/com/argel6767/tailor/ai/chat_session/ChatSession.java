package com.argel6767.tailor.ai.chat_session;

import com.argel6767.tailor.ai.message.Message;
import com.argel6767.tailor.ai.user.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ChatSessionPage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long chatSessionId;

    @Column(nullable = false)
    private String chatSessionName = "New Chat";

    private String s3FileKey;
    
    @Timestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "chatSession")
    @JsonManagedReference
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

    public String getS3FileKey() {
        return s3FileKey;
    }

    public void setS3FileKey(String s3FileKey) {
        this.s3FileKey = s3FileKey;
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
