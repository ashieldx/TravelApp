package com.TravelApp.dto;

import java.util.List;

import com.TravelApp.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortDto {
    private String title;
    private double latitude;
    private double longtitude;
    
    private List<Category> categories;
    private List<String> cities;
    
}
