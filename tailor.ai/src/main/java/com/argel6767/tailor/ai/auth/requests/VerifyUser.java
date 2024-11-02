package com.argel6767.tailor.ai.auth.requests;

public class VerifyUser {

    private String email;
    private String verificationToken;

    public VerifyUser(String email, String verificationToken) {
        this.email = email;
        this.verificationToken = verificationToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }


}
