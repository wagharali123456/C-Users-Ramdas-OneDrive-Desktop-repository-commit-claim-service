package com.org.fms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.fms.model.Claim;
import com.org.fms.service.ClaimService;

@RestController
@RequestMapping("/claims")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @PostMapping("/initialize/{claimNumber}")
    public ResponseEntity<String> initializeClaim(@PathVariable String claimNumber) {
        try {
            claimService.initializeClaim(claimNumber);
            return ResponseEntity.ok("Claim Initialization triggered for claim number: " + claimNumber);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error occurred during claim initialization: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Unexpected error occurred during claim initialization: " + ex.getMessage());
        }
    }
    
    // Endpoint to create a new claim
    @PostMapping("/create")
    public ResponseEntity<Claim> createClaim(@RequestBody Claim claim) {
        try {
            // Call the service to create the claim
            Claim createdClaim = claimService.createClaim(claim);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClaim); // Return the created claim with 201 status
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Return 400 if validation fails
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return 500 for unexpected errors
        }
    }

    @PostMapping("/review/{claimNumber}/{currentState}/{condition}")
    public ResponseEntity<String> reviewClaim(@PathVariable String claimNumber, 
                                              @PathVariable String currentState, 
                                              @PathVariable String condition) {
        try {
            claimService.reviewClaim(claimNumber, currentState, condition);
            return ResponseEntity.ok("Claim Review triggered for claim number: " + claimNumber);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error occurred during claim review: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Unexpected error occurred during claim review: " + ex.getMessage());
        }
    }

    @PostMapping("/approve/{claimNumber}/{currentState}/{condition}")
    public ResponseEntity<String> approveClaim(@PathVariable String claimNumber, 
                                               @PathVariable String currentState, 
                                               @PathVariable String condition) {
        try {
            claimService.approveClaim(claimNumber, currentState, condition);
            return ResponseEntity.ok("Claim Approved for claim number: " + claimNumber);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error occurred during claim approval: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Unexpected error occurred during claim approval: " + ex.getMessage());
        }
    }

    @PostMapping("/reject/{claimNumber}/{currentState}/{condition}")
    public ResponseEntity<String> rejectClaim(@PathVariable String claimNumber, 
                                              @PathVariable String currentState, 
                                              @PathVariable String condition) {
        try {
            claimService.rejectClaim(claimNumber, currentState, condition);
            return ResponseEntity.ok("Claim Rejected for claim number: " + claimNumber);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error occurred during claim rejection: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Unexpected error occurred during claim rejection: " + ex.getMessage());
        }
    }

    @PostMapping("/complete/{claimNumber}/{currentState}/{condition}")
    public ResponseEntity<String> completeClaim(@PathVariable String claimNumber, 
                                                @PathVariable String currentState, 
                                                @PathVariable String condition) {
        try {
            claimService.completeClaim(claimNumber, currentState, condition);
            return ResponseEntity.ok("Claim Process Completed for claim number: " + claimNumber);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error occurred during claim completion: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Unexpected error occurred during claim completion: " + ex.getMessage());
        }
    }
}
