package com.argel6767.tailor.ai.chat_session;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ChatSession {

    @Id
    private Long chatSessionId;

    public void setChatSessionId(Long chatSessionId) {
        this.chatSessionId = chatSessionId;
    }

    public Long getChatSessionId() {
        return chatSessionId;
    }
}
