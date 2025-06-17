package com.ST2I.spring.login.repository;

import com.ST2I.spring.login.models.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username); // Find user by username
  Boolean existsByUsername(String username); // Check if username exists
  Boolean existsByEmail(String email); // Check if email exists
}