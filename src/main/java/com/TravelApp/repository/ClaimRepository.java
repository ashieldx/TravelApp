package com.TravelApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Claim;
import com.TravelApp.entity.Post;

import java.util.List;



public interface ClaimRepository extends JpaRepository<Claim, Integer>{
    Page<Claim> findByStatus(Pageable pageable, String status);

    List<Claim> findByPost(Post post);
}
