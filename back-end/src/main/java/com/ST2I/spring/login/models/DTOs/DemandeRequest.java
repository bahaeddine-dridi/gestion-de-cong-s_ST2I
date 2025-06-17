package com.ST2I.spring.login.models.DTOs;



import com.ST2I.spring.login.models.ENUMs.TypeConge;
import java.util.Date;

public class DemandeRequest {
    private Date date_debut;
    private Date date_fin;
    private TypeConge type;

    // Getters and Setters
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

    public TypeConge getType() {
        return type;
    }

    public void setType(TypeConge type) {
        this.type = type;
    }
}