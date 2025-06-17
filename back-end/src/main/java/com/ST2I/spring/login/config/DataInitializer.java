package com.ST2I.spring.login.config;

import com.ST2I.spring.login.models.ENUMs.ERole;
import com.ST2I.spring.login.models.Entities.Role;
import com.ST2I.spring.login.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository, DataSource dataSource) {
        return args -> {
            // Initialize roles
            if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_USER));
            }
            if (roleRepository.findByName(ERole.ROLE_MODERATOR).isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_MODERATOR));
            }
            if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_ADMIN));
            }

            // Ensure the `initial_date` column exists in the `solde_conge` table
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            try {
                jdbcTemplate.execute("ALTER TABLE solde_conge ADD COLUMN IF NOT EXISTS initial_date DATE NOT NULL DEFAULT CURRENT_DATE");
                System.out.println("Column 'initial_date' added to 'solde_conge' table (if it did not exist).");
            } catch (Exception e) {
                System.err.println("Error adding column 'initial_date' to 'solde_conge' table: " + e.getMessage());
            }
        };
    }
}