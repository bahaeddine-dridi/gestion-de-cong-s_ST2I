package com.ST2I.spring.login.controllers;

import com.ST2I.spring.login.models.DTOs.DemandeResponse;
import com.ST2I.spring.login.models.DTOs.EquipeRequest;
import com.ST2I.spring.login.models.DTOs.EquipeResponse;
import com.ST2I.spring.login.Services.EquipeService;
import com.ST2I.spring.login.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    // CREATE: Create a new team and add users
    @PostMapping
    public ResponseEntity<EquipeResponse> createEquipe(@RequestBody EquipeRequest equipeRequest) {
        EquipeResponse equipe = equipeService.createEquipe(equipeRequest);
        return ResponseEntity.ok(equipe);
    }

    // READ: Get all teams
    @GetMapping
    public ResponseEntity<List<EquipeResponse>> getAllEquipes() {
        List<EquipeResponse> equipes = equipeService.getAllEquipes();
        return ResponseEntity.ok(equipes);
    }

    // READ: Get team by ID
    @GetMapping("/{id}")
    public ResponseEntity<EquipeResponse> getEquipeById(@PathVariable Long id) {
        EquipeResponse equipe = equipeService.getEquipeById(id);
        return ResponseEntity.ok(equipe);
    }

    // UPDATE: Update an existing team
    @PutMapping("/{id}")
    public ResponseEntity<EquipeResponse> updateEquipe(@PathVariable Long id, @RequestBody EquipeRequest equipeRequest) {
        EquipeResponse updatedEquipe = equipeService.updateEquipe(id, equipeRequest);
        return ResponseEntity.ok(updatedEquipe);
    }

    // DELETE: Delete a team by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipe(@PathVariable Long id) {
        equipeService.deleteEquipe(id);
        return ResponseEntity.noContent().build();
    }

    // GET: Fetch leave requests for users in the chef's team
    @GetMapping("/chef/demandes")
    public ResponseEntity<List<DemandeResponse>> getDemandesForChefEquipe() {
        // Get the authenticated user (chef_equipe)
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // Fetch leave requests for users in the chef's team
        List<DemandeResponse> demandes = equipeService.getDemandesForChefEquipe(userDetails.getId());

        return ResponseEntity.ok(demandes);
    }
}