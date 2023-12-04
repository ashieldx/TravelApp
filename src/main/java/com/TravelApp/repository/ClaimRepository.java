package com.TravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Claim;

public interface ClaimRepository extends JpaRepository<Claim, Integer>{
    
}
