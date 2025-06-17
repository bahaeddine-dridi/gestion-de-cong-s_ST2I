package com.ST2I.spring.login.models.DTOs;

import com.ST2I.spring.login.models.ENUMs.Statut;
import com.ST2I.spring.login.models.ENUMs.TypeConge;
import java.util.Date;

public class DemandeResponse {
    private Long demande_id;
    private UserResponse user; // Updated to use UserResponse
    private Date date_debut;
    private Date date_fin;
    private Statut statut;
    private TypeConge type;

    // Getters and Setters
    public Long getDemande_id() {
        return demande_id;
    }

    public void setDemande_id(Long demande_id) {
        this.demande_id = demande_id;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public TypeConge getType() {
        return type;
    }

    public void setType(TypeConge type) {
        this.type = type;
    }
}