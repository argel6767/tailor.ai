package com.argel6767.tailor.ai.user.requests;

public class EmailObjectRequest {

    private String email;

    public EmailObjectRequest(String email) {
        this.email = email;
    }
    public EmailObjectRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
