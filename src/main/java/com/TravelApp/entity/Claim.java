package com.TravelApp.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mst_user_claim")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private User user;

    @OneToOne
    private Post post;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "comments")
    private String comments;

    @Column(name = "created_date")
    private LocalDateTime createdDate;  

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<ClaimDetails> claimDetails = new ArrayList<ClaimDetails>();
    
    
}
