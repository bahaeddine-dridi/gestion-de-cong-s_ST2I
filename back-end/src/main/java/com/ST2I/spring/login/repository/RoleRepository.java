package com.ST2I.spring.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ST2I.spring.login.models.ENUMs.ERole;
import com.ST2I.spring.login.models.Entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  /**
   * Finds a role by its name.
   *
   * @param name The name of the role (e.g., ROLE_USER, ROLE_ADMIN).
   * @return An Optional containing the role if found, otherwise empty.
   */
  Optional<Role> findByName(ERole name);

  /**
   * Checks if a role with the given name exists.
   *
   * @param name The name of the role to check.
   * @return True if the role exists, false otherwise.
   */
  boolean existsByName(ERole name);

  /**
   * Finds a role by its ID.
   *
   * @param id The ID of the role.
   * @return An Optional containing the role if found, otherwise empty.
   */
  Optional<Role> findById(Long id);

  /**
   * Deletes a role by its name.
   *
   * @param name The name of the role to delete.
   */
  void deleteByName(ERole name);
}