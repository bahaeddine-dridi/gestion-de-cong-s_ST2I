package com.ST2I.spring.login.models.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class SoldeCongeRequest {

    @NotNull(message = "User ID is required.")
    private Long userId; // ID of the user associated with the solde conge

    @PositiveOrZero(message = "Leave balance must be a positive number or zero.")
    private double congeRepos; // Initial leave balance

    @PositiveOrZero(message = "Sick leave balance must be a positive number or zero.")
    private double congeMaladie; // Sick leave balance

    // Constructors
    public SoldeCongeRequest() {
    }

    public SoldeCongeRequest(Long userId, double congeRepos, double congeMaladie) {
        this.userId = userId;
        this.congeRepos = congeRepos;
        this.congeMaladie = congeMaladie;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getCongeRepos() {
        return congeRepos;
    }

    public void setCongeRepos(double congeRepos) {
        this.congeRepos = congeRepos;
    }

    public double getCongeMaladie() {
        return congeMaladie;
    }

    public void setCongeMaladie(double congeMaladie) {
        this.congeMaladie = congeMaladie;
    }
}