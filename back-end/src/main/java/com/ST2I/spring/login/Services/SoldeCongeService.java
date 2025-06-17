package com.ST2I.spring.login.Services;

import com.ST2I.spring.login.models.DTOs.SoldeCongeRequest;
import com.ST2I.spring.login.models.DTOs.SoldeCongeResponse;
import com.ST2I.spring.login.models.Entities.SoldeConge;
import com.ST2I.spring.login.models.Entities.User;
import com.ST2I.spring.login.repository.SoldeCongeRepository;
import com.ST2I.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class SoldeCongeService {

    @Autowired
    private SoldeCongeRepository soldeCongeRepository;

    @Autowired
    private UserRepository userRepository;

    private static final double LEAVE_ACCUMULATION_RATE = 1.83; // 1.83 days per month
    private static final int MAX_LEAVE_BALANCE = 22; // Maximum leave balance at year-end

    // Create a new SoldeConge record
    @Transactional
    public SoldeCongeResponse createSoldeConge(SoldeCongeRequest soldeCongeRequest) {
        // Validate user existence
        User user = userRepository.findById(soldeCongeRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Error: User with ID " + soldeCongeRequest.getUserId() + " not found."));

        // Check if the user already has a SoldeConge record
        if (soldeCongeRepository.existsByUserId(user.getId())) {
            throw new RuntimeException("Error: User already has a SoldeConge record.");
        }

        // Create and save the new SoldeConge
        SoldeConge soldeConge = new SoldeConge();
        soldeConge.setUser(user);
        soldeConge.setCongeRepos(soldeCongeRequest.getCongeRepos());
        soldeConge.setCongeMaladie(soldeCongeRequest.getCongeMaladie());
        soldeConge.setInitialDate(LocalDate.now()); // Set the initial date

        SoldeConge savedSoldeConge = soldeCongeRepository.save(soldeConge);
        return mapToSoldeCongeResponse(savedSoldeConge);
    }

    // Update an existing SoldeConge record
    @Transactional
    public SoldeCongeResponse updateSoldeConge(Long id, SoldeCongeRequest soldeCongeRequest) {
        // Validate SoldeConge existence
        SoldeConge existingSoldeConge = soldeCongeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: SoldeConge with ID " + id + " not found."));

        // Update the fields
        existingSoldeConge.setCongeRepos(soldeCongeRequest.getCongeRepos());
        existingSoldeConge.setCongeMaladie(soldeCongeRequest.getCongeMaladie());

        // Save the updated SoldeConge
        SoldeConge updatedSoldeConge = soldeCongeRepository.save(existingSoldeConge);
        return mapToSoldeCongeResponse(updatedSoldeConge);
    }
    // Consume sick leave days
    @Transactional
    public void consumeSickLeaveDays(Long id, double daysConsumed) {
        // Validate SoldeConge existence
        SoldeConge soldeConge = soldeCongeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: SoldeConge with ID " + id + " not found."));

        // Validate sick leave consumption
        if (daysConsumed <= 0) {
            throw new RuntimeException("Error: Days consumed must be greater than 0.");
        }
        if (daysConsumed > soldeConge.getCongeMaladie()) {
            throw new RuntimeException("Error: Insufficient sick leave balance.");
        }

        // Subtract the consumed days from the sick leave balance
        double newSickLeaveBalance = soldeConge.getCongeMaladie() - daysConsumed;
        soldeConge.setCongeMaladie(newSickLeaveBalance);

        // Save the updated SoldeConge
        soldeCongeRepository.save(soldeConge);
    }

    // Get SoldeConge record for a specific user
    public SoldeCongeResponse getSoldeCongeByUserId(Long userId) {
        // Validate SoldeConge existence for the user
        SoldeConge soldeConge = soldeCongeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Error: SoldeConge not found for user ID " + userId));
        return mapToSoldeCongeResponse(soldeConge);
    }

    // Calculate accumulated leave based on the initial date
    public double calculateAccumulatedLeave(SoldeConge soldeConge) {
        LocalDate currentDate = LocalDate.now();
        LocalDate initialDate = soldeConge.getInitialDate();

        // Calculate the number of months since the initial date
        long monthsSinceInitialDate = ChronoUnit.MONTHS.between(initialDate, currentDate);

        // Accumulate leave for each month
        double accumulatedLeave = monthsSinceInitialDate * LEAVE_ACCUMULATION_RATE;

        // Add the accumulated leave to the initial congeRepos value
        double totalLeave = soldeConge.getCongeRepos() + accumulatedLeave;

        // Apply the end-of-year reset rule
        if (currentDate.getYear() != initialDate.getYear()) {
            if (totalLeave > MAX_LEAVE_BALANCE) {
                totalLeave = MAX_LEAVE_BALANCE; // Cap at 22 days
            }
        }

        return totalLeave;
    }

    // Get the current leave balance (congeRepos) with accumulated leave
    public double getCurrentLeaveBalance(Long id) {
        // Validate SoldeConge existence
        SoldeConge soldeConge = soldeCongeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: SoldeConge with ID " + id + " not found."));
        return calculateAccumulatedLeave(soldeConge);
    }

    // Consume leave days
    @Transactional
    public void consumeLeaveDays(Long id, double daysConsumed) {
        // Validate SoldeConge existence
        SoldeConge soldeConge = soldeCongeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: SoldeConge with ID " + id + " not found."));

        // Calculate the current leave balance
        double currentLeaveBalance = calculateAccumulatedLeave(soldeConge);

        // Validate leave consumption
        if (daysConsumed <= 0) {
            throw new RuntimeException("Error: Days consumed must be greater than 0.");
        }
        if (daysConsumed > currentLeaveBalance) {
            throw new RuntimeException("Error: Insufficient leave balance.");
        }

        // Subtract the consumed days from the leave balance
        double newLeaveBalance = currentLeaveBalance - daysConsumed;
        soldeConge.setCongeRepos(newLeaveBalance);

        // Save the updated SoldeConge
        soldeCongeRepository.save(soldeConge);
    }

    // Helper method to map SoldeConge entity to SoldeCongeResponse DTO
    private SoldeCongeResponse mapToSoldeCongeResponse(SoldeConge soldeConge) {
        SoldeCongeResponse response = new SoldeCongeResponse();
        response.setId(soldeConge.getId());
        response.setUserId(soldeConge.getUser().getId());
        response.setCongeRepos(soldeConge.getCongeRepos());
        response.setCongeMaladie(soldeConge.getCongeMaladie());
        response.setInitialDate(soldeConge.getInitialDate());
        return response;
    }
}