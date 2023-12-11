package com.TravelApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TravelApp.entity.Notification;
import com.TravelApp.entity.User;
import com.TravelApp.response.CommonResponse;
import com.TravelApp.response.CommonResponseGenerator;
import com.TravelApp.service.NoticationService;
import com.TravelApp.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private NoticationService noticationService;

    @Autowired
    private CommonResponseGenerator commonResponseGenerator;
    
    @GetMapping("/getCurrentUsername")
    public CommonResponse<String> getCurrentUsername(@AuthenticationPrincipal User user){
        return commonResponseGenerator.successResponse(user.getUsername(), "Username Retrieve Success");
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

    @GetMapping("/getAll")
    public List<User> getAll(){
        return userService.getAll();
    }
}
