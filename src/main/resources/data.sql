-- Insert sample claim into the 'claim' table
INSERT INTO claim (claim_number, workflow_id, current_stage_id, current_state_id, created_date, last_modified_date, description, amount, claimant_name)
VALUES ('CLM001', '1', '1', '1', NOW(), NOW(), 'Claim description for processing', 5000, 'John Doe');
