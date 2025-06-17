package com.ST2I.spring.login.models.Entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projets")
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chef_id", nullable = false)
    private User chef;

    // Many-to-many relationship with User
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "projet_users", // Name of the join table
            joinColumns = @JoinColumn(name = "projet_id"), // Foreign key for Projet
            inverseJoinColumns = @JoinColumn(name = "user_id") // Foreign key for User
    )
    private Set<User> users = new HashSet<>();

    // Constructors
    public Projet() {
    }

    public Projet(String name, User chef) {
        this.name = name;
        this.chef = chef;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getChef() {
        return chef;
    }

    public void setChef(User chef) {
        this.chef = chef;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}