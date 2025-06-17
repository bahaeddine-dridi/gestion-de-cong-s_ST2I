package com.ST2I.spring.login.models.DTOs;

import java.time.LocalDate;

public class SoldeCongeResponse {

    private Long id; // ID of the solde conge
    private Long userId; // ID of the user associated with the solde conge
    private double congeRepos; // Leave balance
    private double congeMaladie; // Sick leave balance
    private LocalDate initialDate; // Initial date when congeRepos was set

    // Constructors
    public SoldeCongeResponse() {
    }

    public SoldeCongeResponse(Long id, Long userId, double congeRepos, double congeMaladie, LocalDate initialDate) {
        this.id = id;
        this.userId = userId;
        this.congeRepos = congeRepos;
        this.congeMaladie = congeMaladie;
        this.initialDate = initialDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }
}