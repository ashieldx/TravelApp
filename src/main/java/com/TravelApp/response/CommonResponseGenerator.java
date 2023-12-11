package com.TravelApp.response;

import org.springframework.stereotype.Component;

@Component
public class CommonResponseGenerator {

    public <T> CommonResponse<T> successResponse(T data, String message){
        CommonResponse<T> commonResponse = new CommonResponse<>();

        ResponseSchema responseSchema = new ResponseSchema();
        responseSchema.setStatus("Success");
        responseSchema.setMessage(message);

        commonResponse.setResponseSchema(responseSchema);
        commonResponse.setData(data);
        return commonResponse;
    }

    public <T> CommonResponse<T> errorResponse(T data, String message){
        CommonResponse<T> commonResponse = new CommonResponse<>();
        ResponseSchema responseSchema = new ResponseSchema();
        responseSchema.setStatus("Error");
        responseSchema.setMessage(message);

        commonResponse.setResponseSchema(responseSchema);
        commonResponse.setData(data);
        return commonResponse;
    }


    
}
