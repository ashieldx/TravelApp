package com.TravelApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.entity.Notification;
import com.TravelApp.entity.User;
import com.TravelApp.response.CommonResponse;
import com.TravelApp.response.CommonResponseGenerator;
import com.TravelApp.service.FileService;
import com.TravelApp.service.NoticationService;
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
    private NoticationService noticationService;

    @Autowired
    private CommonResponseGenerator commonResponseGenerator;
    
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
            userResponse = userService.editProfile(user, userRequest, file);     
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to Edit Profile");
        }
        return commonResponseGenerator.errorResponse(userResponse, "Edit User Success");
    }

    @GetMapping("/getUserNotifications")
    public CommonResponse<List<Notification>> getUserNotifications(@AuthenticationPrincipal User user){
        List<Notification> noticationResponse = null;
        try{
            noticationResponse = noticationService.getAllUserNotificaton(user);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to Retreive User Notifications");
        }
        return commonResponseGenerator.successResponse(noticationResponse, "Get User Notification List Success");
    }

    //Get Liked Posts By User

    @GetMapping("/getAll")
    public List<User> getAll(){
        return userService.getAll();
    }
}
