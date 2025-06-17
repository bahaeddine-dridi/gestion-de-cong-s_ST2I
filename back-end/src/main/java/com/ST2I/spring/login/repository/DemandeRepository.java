package com.ST2I.spring.login.repository;

import com.ST2I.spring.login.models.Entities.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByUserId(Long userId);
    List<Demande> findByStatut(String statut);
    List<Demande> findByUserIdIn(List<Long> userIds);
}