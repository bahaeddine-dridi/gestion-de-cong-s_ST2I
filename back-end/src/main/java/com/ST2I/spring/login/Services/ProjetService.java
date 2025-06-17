package com.ST2I.spring.login.Services;

import com.ST2I.spring.login.models.DTOs.DemandeResponse;
import com.ST2I.spring.login.models.DTOs.ProjetRequest;
import com.ST2I.spring.login.models.DTOs.ProjetResponse;
import com.ST2I.spring.login.models.DTOs.UserResponse;
import com.ST2I.spring.login.models.Entities.*;
import com.ST2I.spring.login.repository.DemandeRepository;
import com.ST2I.spring.login.repository.ProjetRepository;
import com.ST2I.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DemandeRepository demandeRepository;

    // CREATE: Create a new project and add users
    public ProjetResponse createProjet(ProjetRequest projetRequest) {
        // Find the chef by ID
        User chef = userRepository.findById(projetRequest.getChefId())
                .orElseThrow(() -> new RuntimeException("Error: Chef not found."));

        // Create the project
        Projet projet = new Projet();
        projet.setName(projetRequest.getName());
        projet.setChef(chef);

        // Add users to the project
        if (projetRequest.getUsers() != null && !projetRequest.getUsers().isEmpty()) {
            List<User> users = userRepository.findAllById(projetRequest.getUsers());
            projet.setUsers(new HashSet<>(users)); // Set the users directly
        }

        // Save the project
        Projet savedProjet = projetRepository.save(projet);
        return mapToProjetResponse(savedProjet);
    }

    // READ: Get all projects
    public List<ProjetResponse> getAllProjets() {
        return projetRepository.findAll().stream()
                .map(this::mapToProjetResponse)
                .collect(Collectors.toList());
    }

    // READ: Get project by ID
    public ProjetResponse getProjetById(Long id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Projet not found."));
        return mapToProjetResponse(projet);
    }

    // UPDATE: Update an existing project
    public ProjetResponse updateProjet(Long id, ProjetRequest projetRequest) {
        Projet existingProjet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Projet not found."));

        // Find the chef by ID
        User chef = userRepository.findById(projetRequest.getChefId())
                .orElseThrow(() -> new RuntimeException("Error: Chef not found."));

        // Update project details
        existingProjet.setName(projetRequest.getName());
        existingProjet.setChef(chef);

        // Update users if provided
        if (projetRequest.getUsers() != null && !projetRequest.getUsers().isEmpty()) {
            List<User> users = userRepository.findAllById(projetRequest.getUsers());
            existingProjet.setUsers(new HashSet<>(users)); // Set the users directly
        }

        // Save the updated project
        Projet updatedProjet = projetRepository.save(existingProjet);
        return mapToProjetResponse(updatedProjet);
    }

    // DELETE: Delete a project by ID
    public void deleteProjet(Long id) {
        projetRepository.deleteById(id);
    }

    // Helper method to map Projet entity to ProjetResponse DTO
    private ProjetResponse mapToProjetResponse(Projet projet) {
        ProjetResponse response = new ProjetResponse();
        response.setId(projet.getId());
        response.setName(projet.getName());

        // Map chef to simplified UserInfo
        ProjetResponse.UserInfo chefInfo = new ProjetResponse.UserInfo(
                projet.getChef().getId(),
                projet.getChef().getUsername(),
                projet.getChef().getEmail() // Add email
        );
        response.setChef(chefInfo);

        // Map users to simplified UserInfo
        Set<ProjetResponse.UserInfo> userInfos = projet.getUsers().stream()
                .map(user -> new ProjetResponse.UserInfo(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail() // Add email
                ))
                .collect(Collectors.toSet());
        response.setUsers(userInfos);

        return response;
    }

    // Add users to a project
    public ProjetResponse addUsersToProject(Long projectId, List<Long> userIds) {
        // Find the project by ID
        Projet projet = projetRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Error: Project not found."));

        // Find the users by their IDs
        List<User> users = userRepository.findAllById(userIds);
        if (users.isEmpty()) {
            throw new RuntimeException("Error: No users found with the provided IDs.");
        }

        // Add users to the project
        projet.getUsers().addAll(users);

        // Save the updated project
        Projet updatedProjet = projetRepository.save(projet);
        return mapToProjetResponse(updatedProjet);
    }

    // Fetch leave requests for users in the chef's project
    public List<DemandeResponse> getDemandesForChefProjet(Long chefId) {
        // Find the project where the user is the chef
        Projet projet = projetRepository.findByChefId(chefId)
                .orElseThrow(() -> new RuntimeException("Error: Project not found for the given chef."));

        // Get all users in the project
        Set<User> users = projet.getUsers();

        // Extract user IDs
        List<Long> userIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        // Fetch leave requests for these users
        List<DemandeResponse> demandes = demandeRepository.findByUserIdIn(userIds).stream()
                .map(this::mapToDemandeResponse)
                .collect(Collectors.toList());

        return demandes;
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