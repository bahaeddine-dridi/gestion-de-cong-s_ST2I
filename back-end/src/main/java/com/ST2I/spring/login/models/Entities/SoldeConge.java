package com.ST2I.spring.login.models.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Entity
@Table(name = "solde_conge")
public class SoldeConge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required.")
    private User user;

    @Column(name = "conge_repos", nullable = false)
    @PositiveOrZero(message = "Leave balance must be a positive number or zero.")
    private double congeRepos;

    @Column(name = "conge_maladie", nullable = false)
    @PositiveOrZero(message = "Sick leave balance must be a positive number or zero.")
    private double congeMaladie;

    @Column(name = "initial_date", nullable = false)
    @NotNull(message = "Initial date is required.")
    private LocalDate initialDate; // Tracks the initial date when congeRepos was set

    // Constructors
    public SoldeConge() {
        this.initialDate = LocalDate.now(); // Initialize with the current date
    }

    public SoldeConge(User user, double congeRepos, double congeMaladie) {
        this.user = user;
        this.congeRepos = congeRepos;
        this.congeMaladie = congeMaladie;
        this.initialDate = LocalDate.now(); // Initialize with the current date
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    // toString method for better logging and debugging
    @Override
    public String toString() {
        return "SoldeConge{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", congeRepos=" + congeRepos +
                ", congeMaladie=" + congeMaladie +
                ", initialDate=" + initialDate +
                '}';
    }
}