package com.argel6767.tailor.ai.auth.requests;

public class ResendEmailDto {

    private String email;

    public ResendEmailDto() {
    }

    public ResendEmailDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
