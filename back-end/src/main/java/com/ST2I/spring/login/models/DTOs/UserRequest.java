package com.ST2I.spring.login.models.DTOs;

import com.ST2I.spring.login.models.Entities.Role;
import java.util.Set;

public class UserRequest {
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;

    // Remove the projets field
    // private Set<ProjetRequest> projets;

    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // Remove the getProjets and setProjets methods
    // public Set<ProjetRequest> getProjets() {
    //     return projets;
    // }

    // public void setProjets(Set<ProjetRequest> projets) {
    //     this.projets = projets;
    // }
}