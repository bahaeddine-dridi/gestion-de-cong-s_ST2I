package com.ST2I.spring.login.repository;

import com.ST2I.spring.login.models.Entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    Optional<Projet> findByName(String name);
    Optional<Projet> findByChefId(Long chefId);
}