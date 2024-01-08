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
    private String fileUrl;
    private String category;
    private String city;
    private LocalDateTime createdDate;

    private float averageRating;
    private int totalRating;
    private int totalRatingThisMonth;
}
