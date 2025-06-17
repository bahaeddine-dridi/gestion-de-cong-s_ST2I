package com.ST2I.spring.login.Services;

import com.ST2I.spring.login.models.DTOs.DemandeResponse;
import com.ST2I.spring.login.models.DTOs.EquipeRequest;
import com.ST2I.spring.login.models.DTOs.EquipeResponse;
import com.ST2I.spring.login.models.DTOs.UserResponse;
import com.ST2I.spring.login.models.Entities.*;
import com.ST2I.spring.login.repository.DemandeRepository;
import com.ST2I.spring.login.repository.EquipeRepository;
import com.ST2I.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DemandeRepository demandeRepository;

    // CREATE: Create a new team and add users
    public EquipeResponse createEquipe(EquipeRequest equipeRequest) {
        // Find the chef by ID
        User chef = userRepository.findById(equipeRequest.getChefId())
                .orElseThrow(() -> new RuntimeException("Error: Chef not found."));

        // Create the team
        Equipe equipe = new Equipe();
        equipe.setName(equipeRequest.getName());
        equipe.setChef(chef);

        // Add users to the team
        if (equipeRequest.getUsers() != null && !equipeRequest.getUsers().isEmpty()) {
            List<User> users = userRepository.findAllById(equipeRequest.getUsers());
            equipe.setUsers(new HashSet<>(users)); // Set the users directly
        }

        // Save the team
        Equipe savedEquipe = equipeRepository.save(equipe);
        return mapToEquipeResponse(savedEquipe);
    }

    // READ: Get all teams
    public List<EquipeResponse> getAllEquipes() {
        return equipeRepository.findAll().stream()
                .map(this::mapToEquipeResponse)
                .collect(Collectors.toList());
    }

    // READ: Get team by ID
    public EquipeResponse getEquipeById(Long id) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Equipe not found."));
        return mapToEquipeResponse(equipe);
    }

    // UPDATE: Update an existing team
    public EquipeResponse updateEquipe(Long id, EquipeRequest equipeRequest) {
        Equipe existingEquipe = equipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Equipe not found."));

        // Find the chef by ID
        User chef = userRepository.findById(equipeRequest.getChefId())
                .orElseThrow(() -> new RuntimeException("Error: Chef not found."));

        // Update team details
        existingEquipe.setName(equipeRequest.getName());
        existingEquipe.setChef(chef);

        // Update users if provided
        if (equipeRequest.getUsers() != null && !equipeRequest.getUsers().isEmpty()) {
            List<User> users = userRepository.findAllById(equipeRequest.getUsers());
            existingEquipe.setUsers(new HashSet<>(users)); // Set the users directly
        }

        // Save the updated team
        Equipe updatedEquipe = equipeRepository.save(existingEquipe);
        return mapToEquipeResponse(updatedEquipe);
    }

    // DELETE: Delete a team by ID
    public void deleteEquipe(Long id) {
        equipeRepository.deleteById(id);
    }

    // Helper method to map Equipe entity to EquipeResponse DTO
    private EquipeResponse mapToEquipeResponse(Equipe equipe) {
        EquipeResponse response = new EquipeResponse();
        response.setId(equipe.getId());
        response.setName(equipe.getName());

        // Map chef to simplified UserInfo
        EquipeResponse.UserInfo chefInfo = new EquipeResponse.UserInfo(
                equipe.getChef().getId(),
                equipe.getChef().getUsername(),
                equipe.getChef().getEmail() // Add email
        );
        response.setChef(chefInfo);

        // Map users to simplified UserInfo
        Set<EquipeResponse.UserInfo> userInfos = equipe.getUsers().stream()
                .map(user -> new EquipeResponse.UserInfo(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail() // Add email
                ))
                .collect(Collectors.toSet());
        response.setUsers(userInfos);

        return response;
    }

    // Add users to a team
    public EquipeResponse addUsersToEquipe(Long equipeId, List<Long> userIds) {
        // Find the team by ID
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new RuntimeException("Error: Team not found."));

        // Find the users by their IDs
        List<User> users = userRepository.findAllById(userIds);
        if (users.isEmpty()) {
            throw new RuntimeException("Error: No users found with the provided IDs.");
        }

        // Add users to the team
        equipe.getUsers().addAll(users);

        // Save the updated team
        Equipe updatedEquipe = equipeRepository.save(equipe);
        return mapToEquipeResponse(updatedEquipe);
    }

    // Fetch leave requests for users in the chef's team
    public List<DemandeResponse> getDemandesForChefEquipe(Long chefId) {
        // Find the team where the user is the chef
        Equipe equipe = equipeRepository.findByChefId(chefId)
                .orElseThrow(() -> new RuntimeException("Error: Team not found for the given chef."));

        // Get all users in the team
        Set<User> users = equipe.getUsers();

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