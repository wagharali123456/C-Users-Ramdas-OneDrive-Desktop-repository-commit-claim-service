package com.org.fms.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.org.fms.model.Claim;
import com.org.fms.repository.ClaimRepository;

@Service
public class ClaimService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ClaimRepository claimRepository;

    private final String workflowServiceUrl = "http://localhost:8080"; // Replace with actual workflow service URL

    // Helper method to handle workflow state translation
    @SuppressWarnings("unchecked")
	private void handleWorkflowStateTransition(String claimNumber, String currentState, String condition) {
        // Fetch the claim using claimNumber
        Claim claim = findClaimByClaimNumber(claimNumber);

        if (claim == null) {
            throw new RuntimeException("Claim not found for claim number: " + claimNumber);
        }

        if (claim.getWorkflow_id() == null) {
            throw new RuntimeException("No workflow associated with claim number: " + claimNumber);
        }

        String url = String.format("%s/workflow/translate/%s/%s/%s", workflowServiceUrl, claim.getWorkflow_id(), currentState, condition);
        System.out.println("Triggering workflow state translation: URL=" + url);

        try {
            Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);
            System.out.println("Workflow translation response: " + response);
            if (response != null && "success".equals(response.get("status"))) {
                String nextStageId = (String) response.get("nextStageId");
                String nextStateId = (String) response.get("nextStateId");
                claim.setCurrent_stage_id(nextStageId);
                claim.setCurrent_state_id(nextStateId);
                claimRepository.save(claim);
            } else {
                System.err.println("Failed to transition workflow state for claim number: " + claimNumber);
            }
        } catch (RestClientException e) {
            System.err.println("Error during workflow state transition: " + e.getMessage());
            throw new RuntimeException("Error occurred during workflow translation", e);
        }
    }

    // Initialize a new claim and link it to a workflow
    public void initializeClaim(String claimNumber) {
        Claim claim = findClaimByClaimNumber(claimNumber);
        if (claim == null) {
            throw new RuntimeException("Claim not found for claim number: " + claimNumber);
        }

        if (claim.getWorkflow_id() == null) {
            initializeWorkflowForClaim(claim);
        }

        claimRepository.save(claim);
        System.out.println("Claim initialization triggered for claim number: " + claimNumber);
    }

    // Create a new claim and initialize its workflow
    public Claim createClaim(Claim claim) {
        validateClaim(claim);

        claim.setCreatedDate(new java.util.Date());
        claim.setLastModifiedDate(new java.util.Date());
        Claim savedClaim = claimRepository.save(claim);
        initializeWorkflowForClaim(savedClaim);

        return savedClaim;
    }

    // Helper method to validate claim
    private void validateClaim(Claim claim) {
        if (claim.getClaimNumber() == null || claim.getClaimNumber().isEmpty()) {
            throw new IllegalArgumentException("Claim number is required");
        }
        if (claim.getClaimantName() == null || claim.getClaimantName().isEmpty()) {
            throw new IllegalArgumentException("Claimant name is required");
        }
        if (claim.getAmount() == null || claim.getAmount() <= 0) {
            throw new IllegalArgumentException("Claim amount must be greater than zero");
        }
    }

    // Helper method to initialize workflow for the claim
    @SuppressWarnings("unchecked")
	private void initializeWorkflowForClaim(Claim claim) {
        if (claim.getWorkflow_id() == null || claim.getWorkflow_id().isEmpty()) {
            throw new RuntimeException("Workflow ID is required to initialize the workflow.");
        }

        String url = String.format("%s/workflow/init/%s", workflowServiceUrl, claim.getWorkflow_id());
        System.out.println("Requesting workflow initialization for claim: URL=" + url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonBody = "{\"key\": \"value\"}"; // Adjust as per your actual request
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);
            if (response != null && "success".equals(response.get("status"))) {
                String workflowId = (String) response.get("workflowId");
                String stageId = (String) response.get("stageId");
                String stateId = (String) response.get("stateId");

                claim.setWorkflow_id(workflowId);
                claim.setCurrent_stage_id(stageId);
                claim.setCurrent_state_id(stateId);

                System.out.println("Workflow initialized for claim " + claim.getClaimNumber() + ": Workflow ID = " + workflowId + ", Stage ID = " + stageId + ", State ID = " + stateId);
            } else {
                System.err.println("Failed to initialize workflow for claim " + claim.getClaimNumber());
            }
        } catch (RestClientException e) {
            System.err.println("Error initializing workflow for claim " + claim.getClaimNumber() + ": " + e.getMessage());
            throw new RuntimeException("Error initializing workflow for claim " + claim.getClaimNumber(), e);
        }
    }

    // Method to review a claim
    public void reviewClaim(String claimNumber, String currentState, String condition) {
        handleWorkflowStateTransition(claimNumber, currentState, condition);
        System.out.println("Claim review triggered for claim number: " + claimNumber);
    }

    // Method to approve a claim
    public void approveClaim(String claimNumber, String currentState, String condition) {
        handleWorkflowStateTransition(claimNumber, currentState, condition);
        System.out.println("Claim approved and state transition triggered for claim number: " + claimNumber);
    }

    // Method to reject a claim
    public void rejectClaim(String claimNumber, String currentState, String condition) {
        handleWorkflowStateTransition(claimNumber, currentState, condition);
        System.out.println("Claim rejected and state transition triggered for claim number: " + claimNumber);
    }

    // Method to complete a claim
    public void completeClaim(String claimNumber, String currentState, String condition) {
        handleWorkflowStateTransition(claimNumber, currentState, condition);
        System.out.println("Claim process completed and state transition triggered for claim number: " + claimNumber);
    }

    // Helper method to find claim by claim number
    private Claim findClaimByClaimNumber(String claimNumber) {
        return claimRepository.findByClaimNumber(claimNumber); // Assuming repository fetch logic
    }
}
