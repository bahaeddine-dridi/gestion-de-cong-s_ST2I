package com.ST2I.spring.login.models.DTOs;

import java.util.Set;

public class ProjetResponse {
    private Long id;
    private String name;
    private UserInfo chef; // Simplified chef info
    private Set<UserInfo> users; // Simplified users info

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

    public UserInfo getChef() {
        return chef;
    }

    public void setChef(UserInfo chef) {
        this.chef = chef;
    }

    public Set<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(Set<UserInfo> users) {
        this.users = users;
    }

    // Inner class for simplified user info
    public static class UserInfo {
        private Long id;
        private String username;
        private String email; // Add email field

        public UserInfo(Long id, String username, String email) { // Update constructor
            this.id = id;
            this.username = username;
            this.email = email;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}