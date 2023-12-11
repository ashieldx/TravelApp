package com.TravelApp.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommonResponse<T> {
    private ResponseSchema responseSchema;
    private  T data;
}
