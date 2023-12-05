package com.TravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Claim;
import java.util.List;


public interface ClaimRepository extends JpaRepository<Claim, Integer>{
    List<Claim> findByStatus(String status);
}
