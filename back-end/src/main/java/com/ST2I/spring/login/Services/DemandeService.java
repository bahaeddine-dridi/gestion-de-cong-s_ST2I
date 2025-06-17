package com.ST2I.spring.login.Services;

import com.ST2I.spring.login.models.DTOs.UserResponse;
import com.ST2I.spring.login.models.Entities.Demande;
import com.ST2I.spring.login.models.Entities.Projet;
import com.ST2I.spring.login.models.Entities.User;
import com.ST2I.spring.login.models.DTOs.DemandeRequest;
import com.ST2I.spring.login.models.DTOs.DemandeResponse;
import com.ST2I.spring.login.models.ENUMs.Statut;
import com.ST2I.spring.login.repository.DemandeRepository;
import com.ST2I.spring.login.repository.ProjetRepository;
import com.ST2I.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjetRepository projetRepository; // Inject ProjetRepository

    // CREATE: Create a new demande
    public DemandeResponse createDemande(DemandeRequest demandeRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Demande demande = new Demande();
        demande.setUser(user);
        demande.setDate_debut(demandeRequest.getDate_debut());
        demande.setDate_fin(demandeRequest.getDate_fin());
        demande.setType(demandeRequest.getType());

        Demande savedDemande = demandeRepository.save(demande);
        return mapToDemandeResponse(savedDemande);
    }

    // READ: Get all demandes for a specific user
    public List<DemandeResponse> getUserDemandes(Long userId) {
        return demandeRepository.findByUserId(userId).stream()
                .map(this::mapToDemandeResponse)
                .collect(Collectors.toList());
    }

    // READ: Get all demandes
    public List<DemandeResponse> getAllDemandes() {
        return demandeRepository.findAll().stream()
                .map(this::mapToDemandeResponse)
                .collect(Collectors.toList());
    }

    // READ: Get demande by ID
    public DemandeResponse getDemandeById(Long id) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Demande not found."));
        return mapToDemandeResponse(demande);
    }

    // DELETE: Delete demande by ID
    public void deleteDemandeById(Long id) {
        demandeRepository.deleteById(id);
    }

    // UPDATE: Update demande by ID
    public DemandeResponse updateDemandeById(Long id, DemandeRequest demandeRequest) {
        Demande existingDemande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Demande not found."));

        existingDemande.setDate_debut(demandeRequest.getDate_debut());
        existingDemande.setDate_fin(demandeRequest.getDate_fin());
        existingDemande.setType(demandeRequest.getType());

        Demande updatedDemande = demandeRepository.save(existingDemande);
        return mapToDemandeResponse(updatedDemande);
    }

    // UPDATE: Update the status of a demande (only for chef_projet)
    public DemandeResponse updateDemandeStatut(Long demandeId, Statut statut, Long chefId) {
        // Find the demande
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Error: Demande not found."));

        // Find the project where the user is the chef
        Projet projet = projetRepository.findByChefId(chefId)
                .orElseThrow(() -> new RuntimeException("Error: Project not found for the given chef."));

        // Check if the user making the request is the chef of the project
        if (!projet.getUsers().contains(demande.getUser())) {
            throw new RuntimeException("Error: You can only update leave requests for users in your project.");
        }

        // Update the status of the demande
        demande.setStatut(statut);

        // Save the updated demande
        Demande updatedDemande = demandeRepository.save(demande);

        // Map the updated Demande entity to DemandeResponse DTO
        return mapToDemandeResponse(updatedDemande);
    }

    // Helper method to map Demande entity to DemandeResponse DTO
    private DemandeResponse mapToDemandeResponse(Demande demande) {
        DemandeResponse response = new DemandeResponse();
        response.setDemande_id(demande.getDemande_id());
        response.setDate_debut(demande.getDate_debut());
        response.setDate_fin(demande.getDate_fin());
        response.setStatut(demande.getStatut());
        response.setType(demande.getType());

        // Map User to UserResponse
        User user = demande.getUser();
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setRoles(user.getRoles());

        response.setUser(userResponse);

        return response;
    }
}