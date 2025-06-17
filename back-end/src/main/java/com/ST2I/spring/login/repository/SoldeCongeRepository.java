package com.ST2I.spring.login.repository;

import com.ST2I.spring.login.models.Entities.SoldeConge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SoldeCongeRepository extends JpaRepository<SoldeConge, Long> {

    // Find solde conge by user ID
    Optional<SoldeConge> findByUserId(Long userId);

    // Check if a solde conge exists for a given user ID
    boolean existsByUserId(Long userId);
}