package com.ST2I.spring.login.repository;

import com.ST2I.spring.login.models.Entities.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {
    Optional<Equipe> findByName(String name);
    Optional<Equipe> findByChefId(Long chefId);
}