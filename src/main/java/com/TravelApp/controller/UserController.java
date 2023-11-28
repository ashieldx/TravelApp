package com.TravelApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TravelApp.entity.Notification;
import com.TravelApp.entity.User;
import com.TravelApp.service.NoticationService;
import com.TravelApp.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private NoticationService noticationService;
    
    @GetMapping("/getCurrentUsername")
    public String getCurrentUsername(@AuthenticationPrincipal User user){
        return user.getUsername();
    }

    @GetMapping("/getUserNotifications")
    public List<Notification> getUserNotifications(@AuthenticationPrincipal User user){
        return noticationService.getAllUserNotificaiton(user);
    }

    @GetMapping("/getAll")
    public List<User> getAll(){
        return userService.getAll();
    }
}
