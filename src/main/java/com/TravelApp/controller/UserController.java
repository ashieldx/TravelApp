package com.TravelApp.controller;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.entity.User;
import com.TravelApp.response.CommonResponse;
import com.TravelApp.response.CommonResponseGenerator;
import com.TravelApp.service.FileService;
import com.TravelApp.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CommonResponseGenerator commonResponseGenerator;

    private static final EmailValidator emailValidator = EmailValidator.getInstance();
    
    @GetMapping("/get")
    public CommonResponse<User> getUser(@AuthenticationPrincipal User user){
        return commonResponseGenerator.successResponse(user, "User Retrieve Success");
    }

    @PostMapping("/edit")
    public CommonResponse<User> editUser(@AuthenticationPrincipal User user, @RequestParam("data") String userString, @RequestParam("file") MultipartFile file) throws JsonMappingException, JsonProcessingException{
        User userRequest = new ObjectMapper().readValue(userString, User.class);
        User userResponse = null;
        try{
            fileService.validateFile(file);
            if(!emailValidator.isValid(userRequest.getEmail())){
                return commonResponseGenerator.errorResponse(null, "Invalid Email Format");
            }
            if(!userService.validateEmailEdit(user, userRequest.getEmail())){
                return commonResponseGenerator.errorResponse(null, "Email Taken!");
            }
            userResponse = userService.editProfile(user, userRequest, file);     
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to Edit Profile");
        }
        return commonResponseGenerator.successResponse(userResponse, "Edit User Success");
    }

    //ADMIN ONLY - GET ALL USER DATA
    @GetMapping("/getAll")
    public CommonResponse<List<User>> getAll(){
        List<User> users = null;
        try{
            users = userService.getAll();
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to User List");
        }
        return commonResponseGenerator.successResponse(users, "Get User List Success");

    }
}
