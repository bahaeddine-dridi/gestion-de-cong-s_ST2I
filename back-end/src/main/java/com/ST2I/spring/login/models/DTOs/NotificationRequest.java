package com.ST2I.spring.login.models.DTOs;

public class NotificationRequest {
    private String content;
    private Long userToId; // ID of the recipient (chef or user)
    private Long userFromId; // ID of the sender (user or chef)

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserToId() {
        return userToId;
    }

    public void setUserToId(Long userToId) {
        this.userToId = userToId;
    }

    public Long getUserFromId() {
        return userFromId;
    }

    public void setUserFromId(Long userFromId) {
        this.userFromId = userFromId;
    }
}