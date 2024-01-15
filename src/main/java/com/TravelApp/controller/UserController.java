package com.TravelApp.controller;

import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.dto.PostDto;
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

    @GetMapping("/profile/{username}")
    public CommonResponse<User> getUserProfile(@PathVariable("username") String username){
        User user = null;
        try{
            user = userService.findByUsername(username);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to retrieve profile");
        }
        return commonResponseGenerator.successResponse(user, "Retrieve User Profile Success");
    }
    

    @GetMapping("/posts/{username}")
    public CommonResponse<Page<PostDto>> getUserPosts(
        @PathVariable("username") String username,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue =  "8") int size){
        Page<PostDto> userPosts = null;
        try{
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
            userPosts = userService.getPostsByUser(username, pageable);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to retrieve User posts");
        }
        return commonResponseGenerator.successResponse(userPosts, "Retrieve User Posts Success");
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
