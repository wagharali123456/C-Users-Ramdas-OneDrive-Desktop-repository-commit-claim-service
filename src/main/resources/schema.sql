
-- Create the 'claim' table (given in the provided model)
CREATE TABLE claim (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,       -- Primary key for claim
    claim_number VARCHAR(255) NOT NULL UNIQUE,  -- Unique claim number
    workflow_id VARCHAR(255) NOT NULL,          -- Workflow ID (associating with the workflow table)
    current_stage_id VARCHAR(255),              -- Current stage ID (associating with the workflow_stage table)
    current_state_id VARCHAR(255),              -- Current state ID (associating with the workflow_state table)
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp of when the claim was created
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of when the claim was last modified
    description TEXT,                           -- Claim description
    amount BIGINT,                              -- Amount of the claim
    claimant_name VARCHAR(255)                  -- Claimant's name
);
