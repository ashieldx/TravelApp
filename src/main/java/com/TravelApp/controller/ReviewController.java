package com.TravelApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.dto.SortDto;
import com.TravelApp.entity.Review;
import com.TravelApp.entity.User;
import com.TravelApp.response.CommonResponse;
import com.TravelApp.response.CommonResponseGenerator;
import com.TravelApp.service.FileService;
import com.TravelApp.service.ReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CommonResponseGenerator commonResponseGenerator;

    @PostMapping("/post/{id}")
    public CommonResponse<Review> postReview(@AuthenticationPrincipal User user, @PathVariable("id") Integer postId, @RequestParam("files") MultipartFile[] files, 
        @RequestParam("data") String reviewString) throws JsonMappingException, JsonProcessingException{
        Review review = new ObjectMapper().readValue(reviewString, Review.class);
        Review reviewResponse = null;
        try{
            fileService.validateFiles(files);
            reviewResponse =reviewService.postReview(user, postId, review, files);
        }catch (Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(reviewResponse, "Post Review Success");
    }

    @PutMapping("/edit/{id}")
    public CommonResponse<Review> editPost(@AuthenticationPrincipal User user, @RequestParam("files") MultipartFile[] files,
         @PathVariable("id") Integer id, @RequestParam("data") String reviewString) throws JsonMappingException, JsonProcessingException{
        Review review = new ObjectMapper().readValue(reviewString, Review.class);
        Review reviewResponse = null;
        try{
            fileService.validateFiles(files);
            reviewResponse =reviewService.postReview(user, id, review, files);
        }catch (Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(reviewResponse, "Edit Review Success");
    }

    @PostMapping("/get/{id}")
    public CommonResponse<List<Review>> getPostReviews(@PathVariable("id") Integer id, @RequestBody SortDto sortDto){
        List<Review> reviewResponse = null;
        try{
            reviewResponse = reviewService.getPostReviews(id, sortDto);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to Retrive Reviews");
        }
        return commonResponseGenerator.successResponse(reviewResponse, "Get Post Review List Success");
    }

    @PostMapping("/like/{id}")
    public CommonResponse<Review> likeReview(@AuthenticationPrincipal User user, @PathVariable("id") Integer reviewId){
        Review reviewResponse = null;
        try{
            reviewService.likeReview(user.getId(), reviewId);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(reviewResponse, "Like Review Success");
    }

    @PostMapping("/dislike/{id}")
    public CommonResponse<Review> dislikeReview(@AuthenticationPrincipal User user, @PathVariable("id") Integer reviewId){
        Review reviewResponse = null;
        try{
            reviewService.likeReview(user.getId(), reviewId);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(reviewResponse, "Dislike Review Success");
    }

    @GetMapping("/like/getUsers/{id}")
    public CommonResponse<List<User>> getUsersLikedReview(@PathVariable("id") Integer reviewId){
        List<User> users = null;
        try{
            users = reviewService.getUsersLikedReview(reviewId);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(users, "Retrieve Users Success");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<String> deleteReview(@AuthenticationPrincipal User user, @PathVariable("id") Integer id){
        try{
            reviewService.deleteReview(user, id);
        }catch (Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse("Review Deleted", "Delete Review Success");
    }

    

}
