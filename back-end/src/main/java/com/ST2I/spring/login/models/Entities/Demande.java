package com.ST2I.spring.login.models.Entities;

import com.ST2I.spring.login.models.ENUMs.Statut;
import com.ST2I.spring.login.models.ENUMs.TypeConge;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "demandes")
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long demande_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date_debut;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date_fin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Statut statut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeConge type;
    // Constructors
    public Demande() {
        this.statut = Statut.en_attente; // Default status
    }

    public Long getDemande_id() {
        return demande_id;
    }

    public void setDemande_id(Long demande_id) {
        this.demande_id = demande_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public Demande(User user, Date date_debut, Date date_fin, TypeConge type) {
        this.user = user;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.type = type;
        this.statut = Statut.en_attente;
    }
    }
