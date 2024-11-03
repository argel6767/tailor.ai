package com.argel6767.tailor.ai.user.requests;

public class AddProfessionRequest {
    private String email;
    private String profession;

    public AddProfessionRequest(String email, String profession) {
        this.email = email;
        this.profession = profession;
    }

    public AddProfessionRequest(){}

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession = profession;
    }
}
