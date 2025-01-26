package com.org.fms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.fms.model.Claim;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    Claim findByClaimNumber(String claimNumber);
}