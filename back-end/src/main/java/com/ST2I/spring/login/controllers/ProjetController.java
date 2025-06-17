package com.ST2I.spring.login.controllers;

import com.ST2I.spring.login.models.DTOs.DemandeResponse;
import com.ST2I.spring.login.models.DTOs.ProjetRequest;
import com.ST2I.spring.login.models.DTOs.ProjetResponse;
import com.ST2I.spring.login.Services.ProjetService;
import com.ST2I.spring.login.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
public class ProjetController {

    @Autowired
    private ProjetService projetService;

    // CREATE: Create a new project and add users
    @PostMapping
    public ResponseEntity<ProjetResponse> createProjet(@RequestBody ProjetRequest projetRequest) {
        ProjetResponse projet = projetService.createProjet(projetRequest);
        return ResponseEntity.ok(projet);
    }

    // READ: Get all projects
    @GetMapping
    public ResponseEntity<List<ProjetResponse>> getAllProjets() {
        List<ProjetResponse> projets = projetService.getAllProjets();
        return ResponseEntity.ok(projets);
    }

    // READ: Get project by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjetResponse> getProjetById(@PathVariable Long id) {
        ProjetResponse projet = projetService.getProjetById(id);
        return ResponseEntity.ok(projet);
    }

    // UPDATE: Update an existing project
    @PutMapping("/{id}")
    public ResponseEntity<ProjetResponse> updateProjet(@PathVariable Long id, @RequestBody ProjetRequest projetRequest) {
        ProjetResponse updatedProjet = projetService.updateProjet(id, projetRequest);
        return ResponseEntity.ok(updatedProjet);
    }

    // DELETE: Delete a project by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        projetService.deleteProjet(id);
        return ResponseEntity.noContent().build();
    }

    // GET: Fetch leave requests for users in the chef's project
    @GetMapping("/chef/demandes")
    public ResponseEntity<List<DemandeResponse>> getDemandesForChefProjet() {
        // Get the authenticated user (chef_projet)
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // Fetch leave requests for users in the chef's project
        List<DemandeResponse> demandes = projetService.getDemandesForChefProjet(userDetails.getId());

        return ResponseEntity.ok(demandes);

    }

}