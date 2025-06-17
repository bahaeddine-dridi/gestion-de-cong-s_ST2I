package com.ST2I.spring.login.models.DTOs;

import com.ST2I.spring.login.models.Entities.Role;
import java.util.Set;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;



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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


}