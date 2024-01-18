package com.TravelApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.TravelApp.entity.Notification;
import com.TravelApp.entity.User;
import com.TravelApp.response.CommonResponse;
import com.TravelApp.response.CommonResponseGenerator;
import com.TravelApp.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommonResponseGenerator commonResponseGenerator;

    @GetMapping("/notifications/get")
    public CommonResponse<Page<Notification>> getUserNotifications(
        @AuthenticationPrincipal User user,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue =  "8") int size){
        Page<Notification> notificationResponse = null;
        try{
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
            notificationResponse = notificationService.getAllUserNotificaton(user, pageable);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to Retreive User Notifications");
        }
        return commonResponseGenerator.successResponse(notificationResponse, "Get User Notification List Success");
    }

    @GetMapping("/notifications/readAll")
    public CommonResponse<List<Notification>> readAllNotifications(@AuthenticationPrincipal User user){
        List<Notification> notificationResponse = null;
        try{
            notificationResponse = notificationService.markAllNotificationAsRead(user);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to Read All Notifications");
        }
        return commonResponseGenerator.successResponse(notificationResponse, "Read All Notification Success");

    }

    @GetMapping("/read/{id}")
    public CommonResponse<Notification> readNotification(@PathVariable("id") Integer notificationId) {
        Notification notificationResponse = null;
        try{
            notificationResponse = notificationService.readNotification(notificationId);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to Read  Notification");
        }
        return commonResponseGenerator.successResponse(notificationResponse, "Read Notification Success");
    }
    
    
}
