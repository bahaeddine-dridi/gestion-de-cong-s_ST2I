package com.ST2I.spring.login.models.Entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_to_id", nullable = false)
    private User userTo; // Recipient of the notification

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_from_id", nullable = false)
    private User userFrom; // Sender of the notification

    @Column(name = "creation_date", nullable = false, updatable = false)
    private Instant creationDate;

    // Constructors, getters, setters, and toString

    public Notification() {}

    public Notification(String content, User userTo, User userFrom) {
        this.content = content;
        this.userTo = userTo;
        this.userFrom = userFrom;
        this.creationDate = Instant.now(); // Set creation date to current time
    }

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

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }
}