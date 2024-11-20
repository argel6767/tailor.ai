package com.argel6767.tailor.ai.message;

import com.argel6767.tailor.ai.chat_session.ChatSession;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long messageId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Author author;
    
    @Column(nullable = false, columnDefinition = "text")
    private String body;
    
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "chat_session_id")
    @JsonBackReference
    private ChatSession chatSession;
    
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ChatSession getChatSession() {
        return chatSession;
    }

    public void setChatSession(ChatSession chatSession) {
        this.chatSession = chatSession;
    }

    /*
     * automatically puts timestamp to creation of message
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
