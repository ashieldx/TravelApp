package com.TravelApp.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ResponseSchema {
    private String status;
    private String message;
}
