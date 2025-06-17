package com.ST2I.spring.login.models.DTOs;

import com.ST2I.spring.login.models.Entities.User;

import java.time.Instant;

public class NotificationResponse {
    private Long id;
    private String content;
    private UserResponse userTo; // Recipient details
    private UserResponse userFrom; // Sender details
    private Instant creationDate;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserResponse getUserTo() {
        return userTo;
    }

    public void setUserTo(UserResponse userTo) {
        this.userTo = userTo;
    }

    public UserResponse getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(UserResponse userFrom) {
        this.userFrom = userFrom;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }
}