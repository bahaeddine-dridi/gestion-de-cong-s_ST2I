package com.ST2I.spring.login.models.DTOs;

import java.util.List;

public class ProjetRequest {
    private String name;
    private Long chefId; // Chef's ID
    private List<Long> users; // List of user IDs

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getChefId() {
        return chefId;
    }

    public void setChefId(Long chefId) {
        this.chefId = chefId;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }
}