package com.TravelApp.dto;

import java.util.List;

import com.TravelApp.entity.ClaimDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDto {
    private String message;
    private List<ClaimDetails> claimDetails;
}
