package com.argel6767.tailor.ai.chat_session;

public class ChatSessionNameChangeRequest {
    String name;

    public ChatSessionNameChangeRequest(String name) {
        this.name = name;
    }

    public ChatSessionNameChangeRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
