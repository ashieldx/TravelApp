package com.TravelApp.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Integer id;
    private String title;
    private String description;
    private String fileName;
    private LocalDateTime createdDate;
    private boolean approveFlag;

    private float averageRating;
    private int totalRating;

    public boolean getApproveFlag(){
        return this.approveFlag;
    }
}
