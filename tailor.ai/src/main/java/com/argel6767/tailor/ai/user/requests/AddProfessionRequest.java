package com.argel6767.tailor.ai.user.requests;

public class AddProfessionRequest {
    private String profession;

    public AddProfessionRequest(String email, String profession) {
        this.profession = profession;
    }

    public AddProfessionRequest(){}

    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession = profession;
    }
}
