package com.argel6767.tailor.ai.user;

import com.argel6767.tailor.ai.chat_session.ChatSession;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;

import java.time.LocalDateTime;

import java.util.List;

@Table(name = "users")
@Entity
public class user{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean isEmailVerified;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String profession;

    @Column(nullable = false)
    @Timestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Timestamp
    private LocalDateTime lastLogin;

    @OneToMany(mappedBy="user")
    private List<ChatSession> chatSessions;

    public void setId(Long id) {
        this.userId = id;
    }

    public Long getId() {
        return userId;
    }

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

    public Boolean getIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(Boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
