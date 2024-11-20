package com.argel6767.tailor.ai.openai.requests;

public class AiMessageRequest {

    private Long chatSessionId;
    private String userMessage;

    public AiMessageRequest(Long chatSessionId, String userMessage) {
        this.chatSessionId = chatSessionId;
        this.userMessage = userMessage;
    }

    public AiMessageRequest() {}

    public Long getChatSessionId() {
        return chatSessionId;
    }

    public void setChatSessionId(Long chatSessionId) {
        this.chatSessionId = chatSessionId;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }


}
