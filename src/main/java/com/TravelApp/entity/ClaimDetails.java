package com.TravelApp.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "mst_user_claim_details")
public class ClaimDetails {  

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "renamed_file_name")
    private String fileName;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "url")
    private String url;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
