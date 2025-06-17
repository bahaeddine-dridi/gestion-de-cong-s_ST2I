package com.ST2I.spring.login.Services;

import com.ST2I.spring.login.models.DTOs.UserRequest;
import com.ST2I.spring.login.models.DTOs.UserResponse;
import com.ST2I.spring.login.models.Entities.Role;
import com.ST2I.spring.login.models.Entities.User;
import com.ST2I.spring.login.repository.RoleRepository;
import com.ST2I.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    // CREATE: Create a new user
    public UserResponse createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(encoder.encode(userRequest.getPassword()));

        // Set roles
        Set<Role> roles = new HashSet<>();
        for (Role role : userRequest.getRoles()) {
            Role existingRole = roleRepository.findByName(role.getName())
                    .orElseThrow(() -> new RuntimeException("Error: Role not found."));
            roles.add(existingRole);
        }
        user.setRoles(roles);

        // Save the user
        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    // READ: Get all users
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    // READ: Get user by ID
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        return mapToUserResponse(user);
    }

    // UPDATE: Update an existing user
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        existingUser.setUsername(userRequest.getUsername());
        existingUser.setEmail(userRequest.getEmail());
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            existingUser.setPassword(encoder.encode(userRequest.getPassword()));
        }

        // Update roles
        Set<Role> roles = new HashSet<>();
        for (Role role : userRequest.getRoles()) {
            Role existingRole = roleRepository.findByName(role.getName())
                    .orElseThrow(() -> new RuntimeException("Error: Role not found."));
            roles.add(existingRole);
        }
        existingUser.setRoles(roles);

        // Save the updated user
        User updatedUser = userRepository.save(existingUser);
        return mapToUserResponse(updatedUser);
    }

    // DELETE: Delete a user by ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Helper method to map User entity to UserResponse DTO
    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRoles(user.getRoles());
        return response;
    }
}