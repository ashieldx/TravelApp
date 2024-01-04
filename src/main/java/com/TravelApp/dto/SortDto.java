package com.TravelApp.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SortDto {

    private String field;
    private String direction;
    
}
