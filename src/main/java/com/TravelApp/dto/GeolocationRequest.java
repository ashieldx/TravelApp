package com.TravelApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeolocationRequest {
    private String label;
    private double longtitude;
    private double latitude;
}
