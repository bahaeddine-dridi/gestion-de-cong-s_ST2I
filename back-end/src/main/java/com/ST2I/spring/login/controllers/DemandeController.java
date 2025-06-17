package com.ST2I.spring.login.controllers;

import com.ST2I.spring.login.models.DTOs.DemandeRequest;
import com.ST2I.spring.login.models.DTOs.DemandeResponse;
import com.ST2I.spring.login.security.services.UserDetailsImpl;
import com.ST2I.spring.login.Services.DemandeService;
import com.ST2I.spring.login.models.ENUMs.Statut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/demandes")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    // CREATE: Create a new leave request
    @PostMapping
    public ResponseEntity<?> createDemande(@RequestBody DemandeRequest demandeRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        DemandeResponse demande = demandeService.createDemande(demandeRequest, userDetails.getId());
        return ResponseEntity.ok(demande);
    }

    // READ: Get all leave requests for the authenticated user
    @GetMapping("/my")
    public ResponseEntity<?> getMyDemandes() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return ResponseEntity.ok(demandeService.getUserDemandes(userDetails.getId()));
    }

    // READ: Get all leave requests (for admin or manager)
    @GetMapping
    public ResponseEntity<?> getAllDemandes() {
        return ResponseEntity.ok(demandeService.getAllDemandes());
    }

    // READ: Get a specific leave request by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getDemandeById(@PathVariable Long id) {
        DemandeResponse demande = demandeService.getDemandeById(id);
        return ResponseEntity.ok(demande);
    }

    // DELETE: Delete a leave request by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDemandeById(@PathVariable Long id) {
        demandeService.deleteDemandeById(id);
        return ResponseEntity.ok("Demande deleted successfully!");
    }

    // UPDATE: Update a leave request by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDemandeById(@PathVariable Long id, @RequestBody DemandeRequest demandeRequest) {
        DemandeResponse updatedDemande = demandeService.updateDemandeById(id, demandeRequest);
        return ResponseEntity.ok(updatedDemande);
    }

    // PUT: Update the status of a leave request (only for chef_projet)
    @PutMapping("/{id}/statut")
    public ResponseEntity<?> updateDemandeStatut(
            @PathVariable Long id, // Demande ID
            @RequestParam Statut statut) { // New status
        // Get the authenticated user (chef_projet)
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // Update the status of the leave request
        DemandeResponse updatedDemande = demandeService.updateDemandeStatut(id, statut, userDetails.getId());

        return ResponseEntity.ok(updatedDemande);
    }
}