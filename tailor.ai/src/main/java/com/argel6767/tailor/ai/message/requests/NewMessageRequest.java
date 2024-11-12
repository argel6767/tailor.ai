package com.argel6767.tailor.ai.message.requests;

import com.argel6767.tailor.ai.message.Author;

public class NewMessageRequest {

    private String message;
    private Author author;

    public NewMessageRequest(String message, Author author) {
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
