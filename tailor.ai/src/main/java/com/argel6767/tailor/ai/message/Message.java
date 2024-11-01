package com.argel6767.tailor.ai.message;

import com.argel6767.tailor.ai.chat_session.ChatSession;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long messageId;
    
    @Column(nullable = false)
    private Enum<Author> author;
    
    @Column(nullable = false, columnDefinition = "text")
    private String body;
    
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "chat_session_id")
    private ChatSession chatSession;
    
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Enum<Author> getAuthor() {
        return author;
    }

    public void setAuthor(Enum<Author> author) {
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

}
